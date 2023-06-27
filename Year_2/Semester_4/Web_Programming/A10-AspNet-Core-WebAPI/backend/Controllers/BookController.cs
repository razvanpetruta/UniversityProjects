using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Library.Models;
using Library.Data;

namespace Library.Controllers
{
    [Route("api/books")]
    [ApiController]
    public class BookController : ControllerBase
    {
        private readonly ApiContext _context;

        public BookController(ApiContext context)
        { 
            _context = context; 
        }

        [HttpGet("{id}")]
        public JsonResult Get(int id)
        {
            var book = _context.Books.Find(id);

            if (book == null)
            {
                return new JsonResult(NotFound());
            }

            return new JsonResult(Ok(book));
        }

        [HttpGet]
        public JsonResult GetAll() 
        {
            var books = _context.Books.ToList();

            return new JsonResult(books);
        }

        [HttpGet("genres")]
        public JsonResult GetGenres()
        {
            var genres = _context.Books
                .Select(book => book.Genre)
                .Distinct()
                .ToList();

            return new JsonResult(genres);
        }

        [HttpGet("genre/{genre}")]
        public JsonResult GetByGenre(string genre)
        {
            var books = _context.Books
                .Where(book => book.Genre == genre)
                .ToList();

            return new JsonResult(books);
        }


        [HttpPost]
        public JsonResult Create(Book book) 
        {
            var sessionRole = HttpContext.Session.GetString("Role");

            if (sessionRole != "ADMIN")
            {
                return new JsonResult(Forbid());
            }

            if (book.Title?.Length < 1)
            {
                return new JsonResult(BadRequest("Invalid title"));
            }

            if (book.Author?.Length < 1)
            {
                return new JsonResult(BadRequest("Invalid author"));
            }

            _context.Books.Add(book);
            _context.SaveChanges();
            return new JsonResult(Ok(book));
        }

        [HttpPut("{id}")]
        public JsonResult Update(int id, Book updatedBook)
        {
            var sessionRole = HttpContext.Session.GetString("Role");

            if (sessionRole != "ADMIN")
            {
                return new JsonResult(Forbid());
            }

            var book = _context.Books.Find(id);

            if (book == null)
            {
                return new JsonResult(NotFound());
            }

            // Update the properties of the existing book entity with the values from the updated book object
            book.Title = updatedBook.Title;
            book.Author = updatedBook.Author;
            book.NoPages = updatedBook.NoPages;
            book.Price = updatedBook.Price;
            book.Genre = updatedBook.Genre;

            _context.SaveChanges();

            return new JsonResult(Ok(book));
        }


        [HttpDelete("{id}")] 
        public JsonResult Delete(int id) 
        {
            var sessionRole = HttpContext.Session.GetString("Role");

            if (sessionRole != "ADMIN")
            {
                return new JsonResult(Forbid());
            }

            var book = _context.Books.Find(id);

            if (book == null)
            {
                return new JsonResult(NotFound());
            }

            _context.Books.Remove(book);
            _context.SaveChanges();

            return new JsonResult(NoContent());
        }
    }
}
