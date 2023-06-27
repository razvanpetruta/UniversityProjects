<?php
    require_once('auth.php');
    
    if ($role != 'ADMIN') {
        exit();
    }

    $conn = mysqli_connect("localhost", "root", "", "testdb");

    if (!$conn) {
        die("Connection failed: " . mysqli_connect_error());
    }

    $rentalID = $_POST['id'];

    $stmt = mysqli_prepare($conn, "DELETE FROM rentals WHERE ID = ?");
    mysqli_stmt_bind_param($stmt, "d", $rentalID);

    if (mysqli_stmt_execute($stmt)) {
        $response = array('message' => "Rental $rentalID has been rejected.");
        echo json_encode($response);
    } else {
        $response = array('message' => "Error: approving rental: " . mysqli_error($conn));
        echo json_encode($response);
    }

    mysqli_close($conn);
?>