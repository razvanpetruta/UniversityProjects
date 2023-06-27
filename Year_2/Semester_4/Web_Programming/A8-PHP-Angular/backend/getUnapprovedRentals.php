<?php
    header("Access-Control-Allow-Origin: http://localhost:4200");
    header("Access-Control-Allow-Headers: Content-Type");
    header("Access-Control-Allow-Methods: GET");
    
	$conn = mysqli_connect("localhost", "root", "", "testdb");

	if (!$conn) {
		die("Connection failed: " . mysqli_connect_error());
	}

	$result = mysqli_query($conn, "SELECT rentals.ID, books.title, users.username, rentals.startDate, rentals.returnDate, rentals.approved 
    FROM rentals INNER JOIN books ON books.ID = rentals.bookID 
    INNER JOIN users ON users.ID = rentals.userID WHERE rentals.approved = 0");

	$books = array();
	while ($row = mysqli_fetch_assoc($result)) {
		array_push($books, $row);
	}

	mysqli_close($conn);

	$response = array('rentals' => $books);

    echo json_encode($response);
?>