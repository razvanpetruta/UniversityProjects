<?php
	session_start();

	$role = $_SESSION['role'];
    $username = $_SESSION['user'];

	$conn = mysqli_connect("localhost", "root", "", "testdb");

	if (!$conn) {
		die("Connection failed: " . mysqli_connect_error());
	}

	if ($role == 'USER') {
        $result = mysqli_query($conn, "SELECT books.title, rentals.startDate, rentals.returnDate, rentals.approved 
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

	if (count($books) == 0) {
        echo "No rentals found for you";
    } else {
        echo "<table>";
        echo "<thead>";
        if ($role == 'ADMIN') {
            echo "<tr><th>Title</th><th>User</th><th>Start Date</th><th>Return Date</th><th>Approved</th>";
        } else {
            echo "<tr><th>Title</th><th>Start Date</th><th>Return Date</th><th>Approved</th>";
        }
        echo "</thead>";
        echo "<tbody>";
        foreach ($books as $book) {
            echo "<tr>";
            echo "<td>" . $book['title'] . "</td>";
            if ($role == 'ADMIN') {
                echo "<td>" . $book['username'] . "</td>";
            }
            echo "<td>" . $book['startDate'] . "</td>";
            echo "<td>" . $book['returnDate'] . "</td>";
            if ($book['approved'] == 0) {
                echo "<td>" . 'NO' . "</td>";
            } else {
                echo "<td>" . 'YES' . "</td>";
            }
            echo "</tr>";
        }
        echo "</tbody>";
        echo "</table>";
    }
?>