<?php
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

	if (count($books) == 0) {
        echo "No rentals found";
    } else {
        echo "<table>";
        echo "<thead>";
        echo "<tr><th>Title</th><th>User</th><th>Start Date</th><th>Return Date</th><th>Approved</th>";
        echo "</thead>";
        echo "<tbody>";
        foreach ($books as $book) {
            echo "<tr>";
            echo "<td style='display: none;'>" . $book['ID'] . "</td>";
            echo "<td>" . $book['title'] . "</td>";
            echo "<td>" . $book['username'] . "</td>";
            echo "<td>" . $book['startDate'] . "</td>";
            echo "<td>" . $book['returnDate'] . "</td>";
            echo "<td><button class='approve-rental' data-id='" . $book['ID'] . "'>Approve</button></td>";
            echo "<td><button class='reject-rental' data-id='" . $book['ID'] . "'>Reject</button></td>";
            echo "</tr>";
        }
        echo "</tbody>";
        echo "</table>";
    }
?>