<?php 
	header("Access-Control-Allow-Origin: http://localhost:4200");
	header("Access-Control-Allow-Headers: Content-Type");
	header("Access-Control-Allow-Methods: GET");

	$conn = mysqli_connect("localhost", "root", "", "testdb");

	if (!$conn) {
		die("Connection failed: " . mysqli_connect_error());
	}

	$result = mysqli_query($conn, "SELECT DISTINCT genre FROM books");

	$genres = array();
	while ($row = mysqli_fetch_assoc($result)) {					
		array_push($genres, $row['genre']);
	}

	mysqli_close($conn);
	
	header("Content-Type: application/json");
    echo json_encode($genres);
?>
