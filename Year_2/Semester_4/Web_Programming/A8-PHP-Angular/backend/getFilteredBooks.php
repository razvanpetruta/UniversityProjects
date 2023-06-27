<?php
	header("Access-Control-Allow-Origin: http://localhost:4200");
	header("Access-Control-Allow-Headers: Content-Type");
	header("Access-Control-Allow-Methods: GET");

	$conn = mysqli_connect("localhost", "root", "", "testdb");

	if (!$conn) {
		die("Connection failed: " . mysqli_connect_error());
	}

	$genre = $_GET['genre'];
	$stmt = mysqli_prepare($conn, "SELECT * FROM books WHERE genre = ?");
	mysqli_stmt_bind_param($stmt, "s", $genre);
	mysqli_stmt_execute($stmt);
	$result = mysqli_stmt_get_result($stmt);

	$books = array();
	while ($row = mysqli_fetch_assoc($result)) {
		array_push($books, $row);
	}

	mysqli_stmt_close($stmt);
	mysqli_close($conn);

	header('Content-Type: application/json');
    echo json_encode($books);
?>