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

    $conn = mysqli_connect("localhost", "root", "", "testdb");

    if (!$conn) {
        die("Connection failed: " . mysqli_connect_error());
    }

    $bookId = $_POST['id'];

    $stmt = mysqli_prepare($conn, "DELETE FROM books WHERE ID = ?");
    mysqli_stmt_bind_param($stmt, "d", $bookId);

    if (mysqli_stmt_execute($stmt)) {
        echo "Book with ID $bookId has been deleted successfully.";
    } else {
        echo "Error deleting book: " . mysqli_error($conn);
    }

    mysqli_close($conn);
?>