using ExamDotnet.Model;
using Microsoft.EntityFrameworkCore;
using System.Numerics;

namespace ExamDotnet.Data
{
    public class ApiContext : DbContext
    {
        public DbSet<City> Cities { get; set; }
        public DbSet<Link> Links { get; set; }
        public ApiContext(DbContextOptions<ApiContext> options)
            : base(options)
        {

        }
    }
}
