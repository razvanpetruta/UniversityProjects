const Koa = require('koa');
const Router = require('koa-router');
const bodyParser = require('koa-bodyparser');
const cors = require('@koa/cors');
const app = module.exports = new Koa();
const WebSocket = require('ws');
const http = require('http');

const server = http.createServer(app.callback());
const wss = new WebSocket.Server({ server });

app.use(bodyParser());
app.use(cors());
app.use(middleware);

function middleware(ctx, next) {
  const start = new Date();
  return next().then(() => {
    const ms = new Date() - start;
    console.log(`${start.toLocaleTimeString()} ${ctx.response.status} ${ctx.request.method} ${ctx.request.url} - ${ms}ms`);
  });
}

let dailyReports = [
  { id: 1, title: "Report 1", gratitude: "Grateful for a sunny day", accomplishments: "Completed project", shortcomings: "Missed a meeting", improvementAreas: "Time management", rating: 5, date: "2023-02-19" }
];

function validateReportFields(ctx, next) {
  const { title, gratitude, accomplishments, shortcomings, improvementAreas, rating, date } = ctx.request.body;

  if (!title || !gratitude || !accomplishments || !shortcomings || !improvementAreas || !rating || !date) {
    ctx.status = 400; // Bad Request
    ctx.body = { error: "All fields must be filled" };

    console.log("Validation Error: Missing required fields in the request body", ctx.request.body); 
  }

  console.log("Validation Successful for report:", ctx.request.body); 
  
  return next();
}

function broadcast(data) {
  wss.clients.forEach(client => {
      if (client.readyState === WebSocket.OPEN) {
          client.send(JSON.stringify(data));
      }
  });
}

const router = new Router();

function delay(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

async function simulateDelay(ctx, next) {
  await delay(200); // Delay for 1000 milliseconds (1 second)
  await next(); // Proceed to the next middleware
}

app.use(simulateDelay);

// Get all reports
router.get('/reports', ctx => {
  ctx.body = dailyReports;
});

// Get a specific report by id
router.get('/reports/:id', ctx => {
  const id = parseInt(ctx.params.id);
  const report = dailyReports.find(r => r.id === id);
  if (report) {
      ctx.body = report;
  } else {
      ctx.status = 404;
      ctx.body = { error: "Report not found" };
  }
});

// Create a new report
router.post('/reports', validateReportFields, ctx => {
  const report = ctx.request.body;
  const maxId = dailyReports.reduce((max, report) => report.id > max ? report.id : max, 0);
  report.id = maxId + 1;
  dailyReports.push(report);
  ctx.body = report;
  ctx.status = 201;

  broadcast({ action: 'create', report: report });

  console.log(`New report created:`, report);
});

// Update a report
router.put('/reports/:id', validateReportFields, ctx => {
  const id = parseInt(ctx.params.id);
  let index = dailyReports.findIndex(r => r.id === id);
  if (index !== -1) {
      dailyReports[index] = { ...dailyReports[index], ...ctx.request.body };
      ctx.body = dailyReports[index];

      broadcast({ action: 'update', report: dailyReports[index] });

      console.log(`Report updated (ID: ${id}):`, dailyReports[index]);
  } else {
      ctx.status = 404;
      ctx.body = { error: "Report not found" };

      console.log(`Report not found (ID: ${id})`);
  }
});

// Delete a report
router.del('/reports/:id', ctx => {
  const id = parseInt(ctx.params.id);
  let index = dailyReports.findIndex(r => r.id === id);
  if (index !== -1) {
      dailyReports.splice(index, 1);
      ctx.status = 204;

      broadcast({ action: 'delete', report: { id: id } });

      console.log(`Report deleted (ID: ${id})`);
  } else {
      ctx.status = 404;
      ctx.body = { error: "Report not found" };

      console.log(`Report not found (ID: ${id})`);
  }
});

app.use(router.routes());
app.use(router.allowedMethods());

const port = 3000;
server.listen(port, () => {
    console.log(`Server running on port ${port}`);
});