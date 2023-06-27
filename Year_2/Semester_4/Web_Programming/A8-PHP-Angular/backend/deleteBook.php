<?php
	require_once('auth.php');
    
    if ($role != 'ADMIN') {
        exit();
    }

    $conn = mysqli_connect("localhost", "root", "", "testdb");

    if (!$conn) {
        die("Connection failed: " . mysqli_connect_error());
    }

    $bookId = $_POST['id'];

    $stmt = mysqli_prepare($conn, "DELETE FROM books WHERE ID = ?");
    mysqli_stmt_bind_param($stmt, "d", $bookId);

    if (mysqli_stmt_execute($stmt)) {
        $response['message'] = "Book with ID $bookId has been deleted successfully.";
        echo json_encode($response);
    } else {
        $response['message'] = "Error: deleting book: " . mysqli_error($conn);
        echo json_encode($response);
    }

    mysqli_close($conn);
?>