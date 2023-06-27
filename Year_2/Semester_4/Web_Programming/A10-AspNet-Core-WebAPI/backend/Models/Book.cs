using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;

namespace Library.Models
{
    [Table("books")]
    public class Book
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int ID { get; set; }
        public string? Title { get; set; }
        public string? Author { get; set; }
        public int NoPages { get; set; }
        public int Price { get; set; }
        public string? Genre { get; set; }
    }
}
