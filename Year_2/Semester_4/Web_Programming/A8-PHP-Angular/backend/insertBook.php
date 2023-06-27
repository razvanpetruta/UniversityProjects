<?php
    require_once('auth.php');

    if ($role != 'ADMIN') {
        exit();
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

    if (strlen($title) < 3) {
        exit();
    }
      
    if (strlen($author) < 3) {
        exit();
    }
      
    if ($noPages < 5 || $noPages > 10000) {
        exit();
    }
      
    if ($price > 1000) {
        exit();
    }

    $stmt = mysqli_prepare($conn, "INSERT INTO books(title, author, noPages, price, genre) VALUES (?, ?, ?, ?, ?)");
    mysqli_stmt_bind_param($stmt, "ssdds", $title, $author, $noPages, $price, $genre);

    if (mysqli_stmt_execute($stmt)) {
        echo json_encode(array('message' => 'Successfully added the new book.'));
    } else {
        echo json_encode(array('message' => 'Error: adding a new book.'));
    }

    mysqli_close($conn);
?>
