<?php
    session_start();

    $user = $_SESSION['user'];
    if (!isset($user)) {
        header("Location: loginForm.php");
        return;
    }

    $role = $_SESSION['role'];
    if ($role != 'USER') {
        header("Location: adminPage.php");
        return;
    }

    $conn = mysqli_connect("localhost", "root", "", "testdb");

    if (!$conn) {
        die("Connection failed: " . mysqli_connect_error());
    }

    $bookID = $_POST['id'];

    $stmt = mysqli_prepare($conn, "SELECT ID FROM users WHERE username = ?");
    mysqli_stmt_bind_param($stmt, "s", $user);

    mysqli_stmt_execute($stmt);
    mysqli_stmt_bind_result($stmt, $userID);
    mysqli_stmt_fetch($stmt);

    mysqli_close($conn);

    $conn = mysqli_connect("localhost", "root", "", "testdb");

    $query = "SELECT COUNT(*) FROM rentals WHERE bookID = ? AND rentals.returnDate >= CURDATE() AND approved = 1";
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

    $today = date("Y-m-d");
    $nextMonth = date("Y-m-d", strtotime("+1 month"));
    $approved = 0;

    $stmt = mysqli_prepare($conn, "INSERT INTO rentals(userID, bookID, startDate, returnDate, approved) VALUES (?, ?, ?, ?, ?)");
    mysqli_stmt_bind_param($stmt, "ddssd", $userID, $bookID, $today, $nextMonth, $approved);

    if (mysqli_stmt_execute($stmt)) {
        echo "Book with ID $bookID has been requested for renting.";
    } else {
        echo "Error requesting the book: " . mysqli_error($conn);
    }

    mysqli_close($conn);
?>