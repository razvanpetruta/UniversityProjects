using System.Data;
using System.Data.SqlClient;
using System.Configuration;

namespace BicycleApp2
{
    public partial class Form1 : Form
    {

        SqlConnection sqlConnection = new SqlConnection(ConfigurationManager.ConnectionStrings["ConnectionString"].ConnectionString.ToString());
        SqlDataAdapter dataAdapter = new SqlDataAdapter();
        DataSet parentDataSet = new DataSet();
        DataSet childDataSet = new DataSet();

        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            manageNamesAndChildLabels();
            populateParentGridView();
        }
        private void manageNamesAndChildLabels()
        {
            parentLabel.Text = ConfigurationManager.AppSettings["ParentTableName"].ToString();
            childLabel.Text = ConfigurationManager.AppSettings["ChildTableName"].ToString();

            List<string> ColumnNames = new List<string>(ConfigurationManager.AppSettings["ChildColumnNames"].Split(','));
            List<string> ColumnTypes = new List<string>(ConfigurationManager.AppSettings["ChildColumnTypes"].Split(','));
            int i = 0;
            int pointX = 30;
            int pointY = 40;
            childPanel.Controls.Clear();
            foreach (string column in ColumnNames)
            {
                // add specific labels
                Label l = new Label();
                l.Text = column;
                l.Name = column + "Label";
                l.Width = 400;
                l.Location = new Point(pointX, pointY);
                l.Visible = true;
                l.Parent = childPanel;
                pointY += 30;

                // check for date type
                if (ColumnTypes[i] == "0")
                {
                    TextBox a = new TextBox();
                    a.Name = column;
                    a.Location = new Point(pointX, pointY);
                    a.Visible = true;
                    a.Width = 400;
                    a.Parent = childPanel;
                    childPanel.Show();
                    pointY += 30;
                }
                if (ColumnTypes[i] == "1")
                {
                    DateTimePicker a = new DateTimePicker();
                    a.Name = column;
                    a.Location = new Point(pointX, pointY);
                    a.Visible = true;
                    a.Parent = childPanel;
                    childPanel.Show();
                    pointY += 30;
                }
                i += 1;
            }

        }

        private void populateParentGridView()
        {
            try
            {
                sqlConnection.Open();
                dataAdapter.SelectCommand = new SqlCommand(ConfigurationManager.AppSettings["ParentSelectQuery"].ToString(), sqlConnection);

                parentDataSet.Clear();
                dataAdapter.Fill(parentDataSet);
                parentGridView.DataSource = parentDataSet.Tables[0];
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
            finally
            {
                if (sqlConnection.State == ConnectionState.Open)
                {
                    sqlConnection.Close();
                }
            }
        }

        private void populateChildGridView()
        {
            try
            {
                sqlConnection.Open();
                dataAdapter.SelectCommand = new SqlCommand(ConfigurationManager.AppSettings["ChildSelectQuery"].ToString(), sqlConnection);

                int parentId = int.Parse(parentGridView.SelectedRows[0].Cells[ConfigurationManager.AppSettings["ParentKeyName"].ToString()].Value.ToString());
                dataAdapter.SelectCommand.Parameters.Add("@parentKey", SqlDbType.Int).Value = parentId;
                childDataSet.Clear();
                dataAdapter.Fill(childDataSet);
                childGridView.DataSource = childDataSet.Tables[0];
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
            finally
            {
                if (sqlConnection.State == ConnectionState.Open)
                {
                    sqlConnection.Close();
                }
            }
        }

        private void addButton_Click(object sender, EventArgs e)
        {
            try
            {
                dataAdapter.InsertCommand = new SqlCommand(ConfigurationManager.AppSettings["ChildInsertQuery"].ToString(), sqlConnection);

                int parentID = int.Parse(parentGridView.SelectedRows[0].Cells[ConfigurationManager.AppSettings["ParentKeyName"].ToString()].Value.ToString());
                dataAdapter.InsertCommand.Parameters.Add("@parentKey", SqlDbType.Int).Value = parentID;

                List<string> ColumnNames = new List<string>(ConfigurationManager.AppSettings["ChildColumnNames"].Split(','));
                List<string> ColumnTypes = new List<string>(ConfigurationManager.AppSettings["ChildColumnTypes"].Split(','));

                int i = 1;
                foreach (string column in ColumnNames)
                {
                    if (ColumnTypes[i - 1] == "0")
                    {
                        TextBox textBox = (TextBox)childPanel.Controls[column];
                        dataAdapter.InsertCommand.Parameters.Add("@column" + i, SqlDbType.VarChar).Value = textBox.Text.Replace(",", ".");
                    }
                    if (ColumnTypes[i - 1] == "1")
                    {
                        DateTimePicker dateTimePicker = (DateTimePicker)childPanel.Controls[column];
                        dataAdapter.InsertCommand.Parameters.Add("@column" + i, SqlDbType.Date).Value = dateTimePicker.Value.Date;
                    }
                    i++;
                }

                sqlConnection.Open();
                dataAdapter.InsertCommand.ExecuteNonQuery();
                MessageBox.Show("Inserted successfully into the database!");
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
            finally
            {
                if (sqlConnection.State == ConnectionState.Open)
                {
                    sqlConnection.Close();
                    populateChildGridView();
                }
            }
        }

        private void updateButton_Click(object sender, EventArgs e)
        {
            try
            {
                dataAdapter.UpdateCommand = new SqlCommand(ConfigurationManager.AppSettings["ChildUpdateQuery"].ToString(), sqlConnection);

                int childId = int.Parse(childGridView.SelectedRows[0].Cells[ConfigurationManager.AppSettings["ChildKeyName"].ToString()].Value.ToString());
                dataAdapter.UpdateCommand.Parameters.Add("@childKey", SqlDbType.Int).Value = childId;

                List<string> ColumnNames = new List<string>(ConfigurationManager.AppSettings["ChildColumnNames"].Split(','));
                List<string> ColumnTypes = new List<string>(ConfigurationManager.AppSettings["ChildColumnTypes"].Split(','));

                int i = 1;
                foreach (string column in ColumnNames)
                {
                    if (ColumnTypes[i - 1] == "0")
                    {
                        TextBox textBox = (TextBox)childPanel.Controls[column];
                        dataAdapter.UpdateCommand.Parameters.Add("@column" + i, SqlDbType.VarChar).Value = textBox.Text.Replace(",", ".");
                    }
                    if (ColumnTypes[i - 1] == "1")
                    {
                        DateTimePicker dateTimePicker = (DateTimePicker)childPanel.Controls[column];
                        dataAdapter.UpdateCommand.Parameters.Add("@column" + i, SqlDbType.Date).Value = dateTimePicker.Value.Date;
                    }
                    i++;
                }

                sqlConnection.Open();
                dataAdapter.UpdateCommand.ExecuteNonQuery();
                MessageBox.Show("Updated successfully into the database!");
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
            finally
            {
                if (sqlConnection.State == ConnectionState.Open)
                {
                    sqlConnection.Close();
                    populateChildGridView();
                }
            }
        }

        private void deleteButton_Click(object sender, EventArgs e)
        {
            try
            {
                dataAdapter.DeleteCommand = new SqlCommand(ConfigurationManager.AppSettings["ChildDeleteQuery"].ToString(), sqlConnection);
                int childId = int.Parse(childGridView.SelectedRows[0].Cells[ConfigurationManager.AppSettings["ChildKeyName"].ToString()].Value.ToString());
                dataAdapter.DeleteCommand.Parameters.Add("@childKey", SqlDbType.Int).Value = childId;
                sqlConnection.Open();
                dataAdapter.DeleteCommand.ExecuteNonQuery();
                MessageBox.Show("Deleted successfully from the database!");
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
            finally
            {
                if (sqlConnection.State == ConnectionState.Open)
                {
                    sqlConnection.Close();
                    populateChildGridView();
                }
            }
        }

        private void parentGridView_RowHeaderMouseClick(object sender, DataGridViewCellMouseEventArgs e)
        {
            populateChildGridView();
        }

        private void childGridView_RowHeaderMouseClick(object sender, DataGridViewCellMouseEventArgs e)
        {
            List<string> ColumnNames = new List<string>(ConfigurationManager.AppSettings["ChildColumnNames"].Split(','));
            List<string> ColumnTypes = new List<string>(ConfigurationManager.AppSettings["ChildColumnTypes"].Split(','));

            int i = 0;
            foreach (string column in ColumnNames)
            {
                if (ColumnTypes[i] == "0")
                {
                    TextBox textBox = (TextBox)childPanel.Controls[column];
                    textBox.Text = childGridView.CurrentRow.Cells[column].Value.ToString();
                }
                if (ColumnTypes[i] == "1")
                {
                    DateTimePicker dateTimePicker = (DateTimePicker)childPanel.Controls[column];
                    string date = childGridView.CurrentRow.Cells[column].Value.ToString();
                    date = date.Split(" ")[0];
                    string[] dayMonthYear = date.Split(".");
                    dateTimePicker.Value = new DateTime(int.Parse(dayMonthYear[2]), int.Parse(dayMonthYear[1]), int.Parse(dayMonthYear[0]));
                }
                i++;
            }
        }
    }
}