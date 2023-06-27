<?php
	session_start();

	$role = $_SESSION['role'];

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


	echo "<table>";
	echo "<thead>";
	echo "<tr><th style='display: none;'>ID</th><th>Title</th><th>Author</th><th>No pages</th><th>Price</th><th>Genre</th></tr>";
	echo "</thead>";
	echo "<tbody>";
	foreach ($books as $book) {
		echo "<tr>";
		echo "<td style='display: none;'>" . $book['ID'] . "</td>";
		echo "<td>" . $book['title'] . "</td>";
		echo "<td>" . $book['author'] . "</td>";
		echo "<td>" . $book['noPages'] . "</td>";
        echo "<td>" . $book['price'] . "</td>";
        echo "<td>" . $book['genre'] . "</td>";
		if ($role == 'ADMIN') {
			echo "<td><button class='edit-book' data-id='" . $book['ID'] . "'>Edit</button></td>";
			echo '<td><button class="delete-book" data-id="' . $book['ID'] . '">Delete</button></td>';
		} else if ($role == 'USER') {
			echo '<td><button class="rent-book" data-id="' . $book['ID'] . '">Request book</button></td>';
		}
		echo "</tr>";
	}
	echo "</tbody>";
	echo "</table>";
?>