using System.Data;
using System.Data.SqlClient;
using System.Globalization;

namespace Lab1
{
    public partial class Form1 : Form
    {
        private SqlConnection DbConnection = new SqlConnection("Data Source=" +
            "RAZVAN_PC\\SQLEXPRESS;Initial Catalog=StreamingMovies;Integrated Security=True");
        private SqlDataAdapter Da = new SqlDataAdapter();
        private DataSet DsWriters = new DataSet();
        private DataSet DsMovies = new DataSet();

        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            populateWritersGridView();
        }

        private void populateWritersGridView()
        {
            try
            {
                DbConnection.Open();

                Da.SelectCommand = new SqlCommand("select * from Writers", DbConnection);

                DsWriters.Clear();
                Da.Fill(DsWriters);
                writersGridView.DataSource = DsWriters.Tables[0];
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
            }
            finally
            {
                if (DbConnection.State == ConnectionState.Open)
                {
                    DbConnection.Close();
                }
            }
        }

        private void populateMoviesGridView()
        {
            try
            {
                DbConnection.Open();

                Da.SelectCommand = new SqlCommand("select * from Movies " +
                    "where WriterID=@id", DbConnection);

                int id = int.Parse(writersGridView.SelectedRows[0].Cells["WriterID"].Value.ToString());

                Da.SelectCommand.Parameters.Add("@id", SqlDbType.Int).Value = id;
                DsMovies.Clear();
                Da.Fill(DsMovies);
                moviesGridView.DataSource = DsMovies.Tables[0];
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
            finally
            {
                if (DbConnection.State == ConnectionState.Open)
                {
                    DbConnection.Close();
                }
            }
        }

        private void writersGridView_RowHeaderMouseClick(object sender, DataGridViewCellMouseEventArgs e)
        {
            populateMoviesGridView();
        }

        private void addButton_Click(object sender, EventArgs e)
        {
            try
            {
                Da.InsertCommand = new SqlCommand("insert into Movies(WriterID," +
                    " MovieTitle, MovieReleaseDate, MovieRating) values " +
                    "(@writerID, @title, @releaseDate, @rating)", DbConnection);

                int writerID = int.Parse(writersGridView.SelectedRows[0].Cells["WriterID"].Value.ToString());
                string title = titleTextBox.Text;
                DateTime releaseDate = datePicker.Value.Date;
                double rating = double.Parse(ratingTextBox.Text);

                Da.InsertCommand.Parameters.Add("@writerID", SqlDbType.Int).Value = writerID;
                Da.InsertCommand.Parameters.Add("@title", SqlDbType.VarChar).Value = title;
                Da.InsertCommand.Parameters.Add("@releaseDate", SqlDbType.Date).Value = releaseDate;
                Da.InsertCommand.Parameters.Add("@rating", SqlDbType.Float).Value = rating;

                DbConnection.Open();

                Da.InsertCommand.ExecuteNonQuery();

                MessageBox.Show("Inserted successfully into the database!");
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
            finally
            {
                if (DbConnection.State == ConnectionState.Open)
                {
                    DbConnection.Close();
                    populateMoviesGridView();
                }
            }
        }

        private void updateButton_Click(object sender, EventArgs e)
        {
            try
            {
                Da.UpdateCommand = new SqlCommand("update Movies " +
                    "set MovieTitle=@title, MovieReleaseDate=@releaseDate, MovieRating=@rating " +
                    "where MovieID=@movieID", DbConnection);

                int movieID = int.Parse(moviesGridView.SelectedRows[0].Cells["MovieID"].Value.ToString());
                string title = titleTextBox.Text;
                DateTime releaseDate = datePicker.Value.Date;
                double rating = double.Parse(ratingTextBox.Text);

                Da.UpdateCommand.Parameters.Add("@movieID", SqlDbType.Int).Value = movieID;
                Da.UpdateCommand.Parameters.Add("@title", SqlDbType.VarChar).Value = title;
                Da.UpdateCommand.Parameters.Add("@releaseDate", SqlDbType.Date).Value = releaseDate;
                Da.UpdateCommand.Parameters.Add("@rating", SqlDbType.Decimal).Value = rating;
                Da.UpdateCommand.Parameters["@rating"].Precision = 10;
                Da.UpdateCommand.Parameters["@rating"].Scale = 2;

                DbConnection.Open();

                Da.UpdateCommand.ExecuteNonQuery();

                MessageBox.Show("Updated successfully into the database!");
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
            finally
            {
                if (DbConnection.State == ConnectionState.Open)
                {
                    DbConnection.Close();
                    populateMoviesGridView();
                }
            }
        }

        private void deleteButton_Click(object sender, EventArgs e)
        {
            try
            {
                Da.DeleteCommand = new SqlCommand("delete from Movies where MovieID=@movieID", DbConnection);

                int movieID = int.Parse(moviesGridView.SelectedRows[0].Cells["MovieID"].Value.ToString());

                Da.DeleteCommand.Parameters.Add("@movieID", SqlDbType.Int).Value = movieID;

                DbConnection.Open();

                Da.DeleteCommand.ExecuteNonQuery();

                MessageBox.Show("Deleted successfully into the database!");
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
            finally
            {
                if (DbConnection.State == ConnectionState.Open)
                {
                    DbConnection.Close();
                    populateMoviesGridView();
                }
            }
        }

        private void moviesGridView_RowHeaderMouseClick(object sender, DataGridViewCellMouseEventArgs e)
        {
            string title = moviesGridView.SelectedRows[0].Cells["MovieTitle"].Value.ToString();
            string releaseDate = moviesGridView.SelectedRows[0].Cells["MovieReleaseDate"].Value.ToString();
            releaseDate = releaseDate.Split(" ")[0];
            string[] dayMonthYear = releaseDate.Split(".");
            double rating = double.Parse(moviesGridView.SelectedRows[0].Cells["MovieRating"].Value.ToString());


            titleTextBox.Text = title;
            datePicker.Value = new DateTime(int.Parse(dayMonthYear[2]), int.Parse(dayMonthYear[1]), int.Parse(dayMonthYear[0]));
            ratingTextBox.Text = rating.ToString();
        }
    }
}