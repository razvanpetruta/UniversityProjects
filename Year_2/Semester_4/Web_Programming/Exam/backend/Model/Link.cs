using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;

namespace ExamDotnet.Model
{
    [Table("links")]
    public class Link
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int ID { get; set; }
        public int Duration { get; set; }
        public int Distance { get; set; }

        [ForeignKey("City1")]
        public int City1ID { get; set; }
        public City? City1 { get; set; }

        [ForeignKey("City2")]
        public int City2ID { get; set; }
        public City? City2 { get; set; }
    }
}
