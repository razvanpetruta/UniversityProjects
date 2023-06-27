using Library.Data;
using Library.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace Library.Controllers
{
    [Route("api/users")]
    [ApiController]
    public class UserController : ControllerBase
    {
        private readonly ApiContext _context;

        public UserController(ApiContext context)
        {
            _context = context;
        }

        [HttpGet("{username}")]
        public JsonResult Get(string username)
        {
            var user = _context.Users.FirstOrDefault(u => u.Username == username);

            if (user == null)
            {
                return new JsonResult(NotFound());
            }

            return new JsonResult(Ok(user));
        }

        [HttpPost("register")]
        public JsonResult Register(User user)
        {
            var existingUser = _context.Users.FirstOrDefault(u => u.Username == user.Username);

            if (existingUser != null)
            {
                return new JsonResult(BadRequest("Username already exists."));
            }

            _context.Users.Add(user);
            _context.SaveChanges();

            HttpContext.Session.SetString("Username", user.Username);

            return new JsonResult(Ok(user));
        }

        [HttpPost("login")]
        public JsonResult Login(User user)
        {
            var existingUser = _context.Users.FirstOrDefault(u => u.Username == user.Username && u.Password == user.Password);

            if (existingUser == null)
            {
                return new JsonResult(BadRequest("Invalid username or password."));
            }

            HttpContext.Session.SetString("Username", existingUser.Username);
            HttpContext.Session.SetString("Role", existingUser.Role);

            return new JsonResult(Ok(existingUser));
        }

        [HttpPost("logout")]
        public JsonResult Logout()
        {
            HttpContext.Session.Remove("Username");
            HttpContext.Session.Remove("Role");

            return new JsonResult(Ok("Logged out successfully."));
        }

        [HttpGet("isLoggedIn")]
        public JsonResult IsLoggedIn()
        {
            var username = HttpContext.Session.GetString("Username");

            if (string.IsNullOrEmpty(username))
            {
                return new JsonResult(false);
            }

            return new JsonResult(true);
        }
    }
}
