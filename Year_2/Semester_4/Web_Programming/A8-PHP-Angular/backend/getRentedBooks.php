<?php
    header("Access-Control-Allow-Origin: http://localhost:4200");
    header("Access-Control-Allow-Headers: Content-Type");
    header("Access-Control-Allow-Methods: GET");

    $session_id = $_GET['session_id'];

    session_id($session_id);
    session_start();

    $conn = mysqli_connect("localhost", "root", "", "testdb");

    $query = "SELECT username, role FROM users WHERE sessionID = ?";
    $stmt = mysqli_prepare($conn, $query);
    mysqli_stmt_bind_param($stmt, "s", $session_id);
    mysqli_stmt_execute($stmt);
    mysqli_stmt_bind_result($stmt, $username, $role);
    mysqli_stmt_fetch($stmt);
    mysqli_stmt_close($stmt);

    mysqli_close($conn);

	$conn = mysqli_connect("localhost", "root", "", "testdb");

	if (!$conn) {
		die("Connection failed: " . mysqli_connect_error());
	}

	if ($role == 'USER') {
        $result = mysqli_query($conn, "SELECT books.title, users.username, rentals.startDate, rentals.returnDate, rentals.approved 
        FROM rentals INNER JOIN books ON books.ID = rentals.bookID 
        INNER JOIN users ON users.ID = rentals.userID WHERE users.username = '$username' AND rentals.approved = 1");
    } else if ($role == 'ADMIN') {
        $result = mysqli_query($conn, "SELECT books.title, users.username, rentals.startDate, rentals.returnDate, rentals.approved 
        FROM rentals INNER JOIN books ON books.ID = rentals.bookID 
        INNER JOIN users ON users.ID = rentals.userID WHERE rentals.approved = 1");
    }

	$books = array();
	while ($row = mysqli_fetch_assoc($result)) {
		array_push($books, $row);
	}

	mysqli_close($conn);

	echo json_encode($books);
?>