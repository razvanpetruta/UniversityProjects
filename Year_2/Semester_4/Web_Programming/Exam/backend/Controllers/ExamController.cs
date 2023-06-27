using ExamDotnet.Data;
using ExamDotnet.Model;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace ExamDotnet.Controllers
{
    [Route("api")]
    [ApiController]
    public class ExamController : ControllerBase
    {
        private readonly ApiContext _context;

        public ExamController(ApiContext context)
        {
            _context = context;
        }

        [HttpGet("cities")]
        public JsonResult GetAllCities()
        {
            var cities = _context.Cities.ToList();

            return new JsonResult(Ok(cities));
        }

        [HttpGet("cities/{id}")]
        public JsonResult GetCityById(int id)
        {
            var city = _context.Cities.Find(id);

            if (city == null) 
            {
                return new JsonResult(NotFound());
            }

            return new JsonResult(Ok(city));
        }

        [HttpGet("neighbours/{id}")]
        public JsonResult GetNeighboursById(int id)
        {
            var allLinks = _context.Links
                .Where(l => l.City2ID == id || l.City1ID == id)
                .Include(l => l.City2)
                .Include(l => l.City1)
                .ToList();

            return new JsonResult(Ok(allLinks));
        }
    }
}
