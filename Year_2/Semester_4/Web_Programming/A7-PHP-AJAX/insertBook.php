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

    $title = $_POST['title'];

    

    $author = $_POST['author'];
    $noPages = $_POST['noPages'];
    $price = $_POST['price'];
    $genre = $_POST['genre'];

    $stmt = mysqli_prepare($conn, "INSERT INTO books(title, author, noPages, price, genre) VALUES (?, ?, ?, ?, ?)");
    mysqli_stmt_bind_param($stmt, "ssdds", $title, $author, $noPages, $price, $genre);

    if (mysqli_stmt_execute($stmt)) {
        echo "<p style='margin: 10rem;'>Successfully added the new book. 
        <a href='adminPage.php'>Go to admin page</a></p>";
    } else {
        echo "Error adding a new book: " . mysqli_error($conn);
    }

    mysqli_close($conn);
?>