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
?>

<!DOCTYPE html>
<html>

<head>
    <title>Add Book</title>

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
        <h1>Add new book</h1>
        <form action="insertBook.php" method="post">
            <table>
                <tr>
                    <td>Title:</td>
                    <td><input type="text" name="title"></td>
                </tr>
                <tr>
                    <td>Author:</td>
                    <td><input type="text" name="author"></td>
                </tr>
                <tr>
                    <td>No Pages:</td>
                    <td><input type="number" name="noPages"></td>
                </tr>
                <tr>
                    <td>Price:</td>
                    <td><input type="number" name="price"></td>
                </tr>
                <tr>
                    <td>Genre:</td>
                    <td><input type="text" name="genre"></td>
                </tr>
                <tr>
                    <td><input type="submit" value="Add"></td>
                </tr>
            </table>
        </form>
    </div>
</body>

</html>