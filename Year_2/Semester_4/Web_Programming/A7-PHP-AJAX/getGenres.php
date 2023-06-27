<?php 
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
	
	printf(json_encode($genres));	
?>
