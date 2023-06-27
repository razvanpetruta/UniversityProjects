<?php
    session_start();

    $user = $_SESSION['user'];
    if (!isset($user)) {
        header("Location: loginForm.php");
        return;
    }

    $role = $_SESSION['role'];
    if ($role != 'ADMIN') {
        header("Location: adminPage.php");
        return;
    }

    $conn = mysqli_connect("localhost", "root", "", "testdb");

    if (!$conn) {
        die("Connection failed: " . mysqli_connect_error());
    }

    $bookId = $_GET['id'];

    $query = "SELECT * FROM books WHERE ID = $bookId";
    $result = mysqli_query($conn, $query);

    if (mysqli_num_rows($result) > 0) {
        $book = mysqli_fetch_assoc($result);
    } else {
        die("Book not found.");
    }

    mysqli_close($conn);
?>

<!DOCTYPE html>
<html>

<head>
    <title>Edit Book</title>

    <style>
        body {
            position: relative;
            width: 100%;
		}

		.container {
			margin: 10rem 30rem;
		}

    </style>
</head>

<body>
    <div class="container">
        <h1>Edit Book</h1>
        <form action="updateBook.php" method="post">
            <table>
                <tr style="display: none;">
                    <td>ID:</td>
                    <td><input name="id" value="<?php echo $book['ID']; ?>"></td>
                </tr>
                <tr>
                    <td>Title:</td>
                    <td><input type="text" name="title" value="<?php echo $book['title']; ?>"></td>
                </tr>
                <tr>
                    <td>Author:</td>
                    <td><input type="text" name="author" value="<?php echo $book['author']; ?>"></td>
                </tr>
                <tr>
                    <td>No Pages:</td>
                    <td><input type="number" name="noPages" value="<?php echo $book['noPages']; ?>"></td>
                </tr>
                <tr>
                    <td>Price:</td>
                    <td><input type="number" name="price" value="<?php echo $book['price']; ?>"></td>
                </tr>
                <tr>
                    <td>Genre:</td>
                    <td><input type="text" name="genre" value="<?php echo $book['genre']; ?>"></td>
                </tr>
                <tr>
                    <td><input type="submit" value="Update"></td>
                </tr>
            </table>
        </form>
    </div>
</body>

</html>