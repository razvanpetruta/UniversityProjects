<?php
	header("Access-Control-Allow-Origin: http://localhost:4200");
	header("Access-Control-Allow-Headers: Content-Type");
	header("Access-Control-Allow-Methods: GET");

    $conn = mysqli_connect("localhost", "root", "", "testdb");

    if (!$conn) {
        die("Connection failed: " . mysqli_connect_error());
    }

    $bookId = $_GET['id'];

    $query = "SELECT * FROM books WHERE ID = $bookId";
    $result = mysqli_query($conn, $query);

    if (mysqli_num_rows($result) > 0) {
        $book = mysqli_fetch_assoc($result);
        echo json_encode($book);
    } else {
        die("Book not found.");
    }

    mysqli_close($conn);
?>