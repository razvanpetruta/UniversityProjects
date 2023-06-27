using Library.Data;
using Library.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Net;

namespace Library.Controllers
{
    [Route("api/rentals")]
    [ApiController]
    public class RentalController : ControllerBase
    {
        private readonly ApiContext _context;

        public RentalController(ApiContext context)
        {
            _context = context;
        }

        [HttpGet("approved/{username}")]
        public JsonResult GetApprovedRentals(string username)
        {
            var sessionRole = HttpContext.Session.GetString("Role");

            if (sessionRole == "ADMIN")
            {
                var rentalsAdmin = _context.Rentals
                    .Where(rental => rental.Approved)
                    .Include(rental => rental.User)
                    .Include(rental => rental.Book)
                    .ToList();

                return new JsonResult(Ok(rentalsAdmin));
            }

            var sessionUsername = HttpContext.Session.GetString("Username");

            if (sessionUsername != username)
            {
                return new JsonResult(Forbid());
            }

            var user = _context.Users.FirstOrDefault(u => u.Username == username);

            if (user == null)
            {
                return new JsonResult(NotFound());
            }

            var rentals = _context.Rentals
                .Where(rental => rental.UserID == user.ID && rental.Approved)
                .Include(rental => rental.User)
                .Include(rental => rental.Book)
                .ToList();

            return new JsonResult(Ok(rentals));
        }

        [HttpGet("unapproved")]
        public JsonResult GetUnapprovedRentals()
        {
            var sessionRole = HttpContext.Session.GetString("Role");

            if (sessionRole != "ADMIN")
            {
                return new JsonResult(Forbid());
            }

            var username = HttpContext.Session.GetString("Username");

            if (string.IsNullOrEmpty(username))
            {
                return new JsonResult(Forbid());
            }

            var user = _context.Users.FirstOrDefault(u => u.Username == username);

            if (user == null)
            {
                return new JsonResult(NotFound());
            }

            var rentals = _context.Rentals
                .Where(rental => !rental.Approved)
                .Include(rental => rental.User)
                .Include(rental => rental.Book)
                .ToList();

            return new JsonResult(Ok(rentals));
        }

        [HttpPost("request")]
        public JsonResult RequestBook(int bookId)
        {
            var sessionRole = HttpContext.Session.GetString("Role");

            if (sessionRole != "USER")
            {
                return new JsonResult(Forbid());
            }

            var username = HttpContext.Session.GetString("Username");

            if (string.IsNullOrEmpty(username))
            {
                return new JsonResult(Forbid());
            }

            var user = _context.Users.FirstOrDefault(u => u.Username == username);

            if (user == null)
            {
                return new JsonResult(NotFound());
            }

            var book = _context.Books.FirstOrDefault(b => b.ID == bookId);

            if (book == null)
            {
                return new JsonResult(NotFound());
            }

            var existingRental = _context.Rentals
                .FirstOrDefault(r => r.BookID == bookId && r.Approved);

            if (existingRental != null)
            {
                return new JsonResult(Conflict("The book is already rented."));
            }

            var rental = new Rental
            {
                UserID = user.ID,
                BookID = book.ID,
                StartDate = DateTime.UtcNow.Date,
                EndDate = DateTime.UtcNow.Date.AddMonths(1),
                Approved = false
            };

            _context.Rentals.Add(rental);
            _context.SaveChanges();

            return new JsonResult(Ok(rental));
        }

        [HttpPost]
        public JsonResult Create(Rental rental)
        {
            var sessionRole = HttpContext.Session.GetString("Role");

            if (sessionRole != "ADMIN")
            {
                return new JsonResult(Forbid());
            }

            var user = _context.Users.Find(rental.UserID);

            if (user == null)
            {
                return new JsonResult(NotFound());
            }

            var book = _context.Books.Find(rental.BookID);

            if (book == null)
            {
                return new JsonResult(NotFound());
            }

            _context.Rentals.Add(rental);
            _context.SaveChanges();
            return new JsonResult(Ok(rental));
        }

        [HttpPut("{rentalId}/approve")]
        public JsonResult ApproveRental(int rentalId)
        {
            var sessionRole = HttpContext.Session.GetString("Role");

            if (sessionRole != "ADMIN")
            {
                return new JsonResult(Forbid());
            }

            var rental = _context.Rentals.FirstOrDefault(r => r.ID == rentalId);

            if (rental == null)
            {
                return new JsonResult(NotFound(rentalId));
            }

            var existingRental = _context.Rentals
                .FirstOrDefault(r => r.BookID == rental.BookID && r.Approved);

            if (existingRental != null)
            {
                return new JsonResult(Conflict("The book is already rented."));
            }

            rental.Approved = true;
            _context.SaveChanges();

            return new JsonResult(Ok(rental));
        }

        [HttpDelete("{rentalId}")]
        public JsonResult RejectRental(int rentalId)
        {
            var sessionRole = HttpContext.Session.GetString("Role");

            if (sessionRole != "ADMIN")
            {
                return new JsonResult(Forbid());
            }

            var rental = _context.Rentals.FirstOrDefault(r => r.ID == rentalId);

            if (rental == null)
            {
                return new JsonResult(NotFound(rentalId));
            }

            _context.Rentals.Remove(rental);
            _context.SaveChanges();

            return new JsonResult(Ok(rentalId));
        }
    }
}
