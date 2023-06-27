using Microsoft.EntityFrameworkCore;
using Library.Models;

namespace Library.Data
{
    public class ApiContext : DbContext
    {
        public DbSet<Book> Books { get; set; }
        public DbSet<User> Users { get; set; }
        public DbSet<Rental> Rentals { get; set; }
        public ApiContext(DbContextOptions<ApiContext> options) 
            : base(options)
        {   
            
        }
    }
}
