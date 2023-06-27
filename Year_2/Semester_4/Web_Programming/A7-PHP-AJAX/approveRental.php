<?php
    session_start();

    $user = $_SESSION['user'];
    if (!isset($user)) {
        header("Location: loginForm.php");
        return;
    }

    $role = $_SESSION['role'];
    if ($role != 'ADMIN') {
        header("Location: adminPage.php");
        return;
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
        echo "This book is already rented";
        return;
    }

    mysqli_close($conn);

    $conn = mysqli_connect("localhost", "root", "", "testdb");

    if (!$conn) {
        die("Connection failed: " . mysqli_connect_error());
    }

    $stmt = mysqli_prepare($conn, "UPDATE rentals SET approved = 1 WHERE rentals.ID = ?");
    mysqli_stmt_bind_param($stmt, "d", $rentalID);

    if (mysqli_stmt_execute($stmt)) {
        echo "Rental $rentalID has been approved.";
    } else {
        echo "Error approving the rental: " . mysqli_error($conn);
    }

    mysqli_close($conn);
?>