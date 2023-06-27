<?php
    require_once('auth.php');

    if ($role != 'ADMIN') {
        exit();
    }

    $conn = mysqli_connect("localhost", "root", "", "testdb");

    if (!$conn) {
        die("Connection failed: " . mysqli_connect_error());
    }

    $bookId = $_POST['id'];
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

    $stmt = mysqli_prepare($conn, "UPDATE books SET title = ?, author = ?, noPages = ?, price = ?, genre = ? WHERE ID = ?");
    mysqli_stmt_bind_param($stmt, "ssddsd", $title, $author, $noPages, $price, $genre, $bookId);

    if (mysqli_stmt_execute($stmt)) {
        $response['message'] = "Book with ID $bookId has been updated successfully.";
        echo json_encode($response);
    } else {
        $response['message'] = "Error: updating book: " . mysqli_error($conn);
        echo json_encode($response);
    }

    mysqli_close($conn);
?>
