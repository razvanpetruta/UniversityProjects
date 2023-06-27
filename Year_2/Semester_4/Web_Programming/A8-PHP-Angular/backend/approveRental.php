<?php
	require_once('auth.php');

    if ($role != 'ADMIN') {
        exit();
    }

    $rentalID = $_POST['id'];

    $conn = mysqli_connect("localhost", "root", "", "testdb");

    $query = "SELECT userID, bookID FROM rentals WHERE ID = ?";
    $stmt = mysqli_prepare($conn, $query);
    mysqli_stmt_bind_param($stmt, "d", $rentalID);
    mysqli_stmt_execute($stmt);
    mysqli_stmt_bind_result($stmt, $userID, $bookID);
    mysqli_stmt_fetch($stmt);
    mysqli_stmt_close($stmt);

    mysqli_close($conn);

    $conn = mysqli_connect("localhost", "root", "", "testdb");

    $query = "SELECT COUNT(*) FROM rentals WHERE bookID = ? AND returnDate >= CURDATE() AND approved = 1";
    $stmt = mysqli_prepare($conn, $query);
    mysqli_stmt_bind_param($stmt, "d", $bookID);
    mysqli_stmt_execute($stmt);
    mysqli_stmt_bind_result($stmt, $rentalCount);
    mysqli_stmt_fetch($stmt);
    mysqli_stmt_close($stmt);

    if ($rentalCount > 0) {
        $response['message'] = 'Error: This book is already rented';
        echo json_encode($response);
        exit();
    }

    mysqli_close($conn);

    $conn = mysqli_connect("localhost", "root", "", "testdb");

    if (!$conn) {
        die("Connection failed: " . mysqli_connect_error());
    }

    $stmt = mysqli_prepare($conn, "UPDATE rentals SET approved = 1 WHERE rentals.ID = ?");
    mysqli_stmt_bind_param($stmt, "d", $rentalID);

    if (mysqli_stmt_execute($stmt)) {
        $response['message'] = "Rental $rentalID has been approved.";
        echo json_encode($response);
    } else {
        $response['error'] = "Error approving the rental: " . mysqli_error($conn);
        echo json_encode($response);
    }

    mysqli_close($conn);
?>