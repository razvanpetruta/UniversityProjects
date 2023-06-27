<?php
    header("Access-Control-Allow-Origin: http://localhost:4200");
    header("Access-Control-Allow-Headers: Content-Type");
    header("Access-Control-Allow-Methods: POST");

    $json = file_get_contents('php://input');
    $_POST = json_decode($json, true);

    $session_id = $_POST['session_id'];

    session_id($session_id);
    session_start();

    $conn = mysqli_connect("localhost", "root", "", "testdb");

    if (!$conn) {
        die("Connection failed: " . mysqli_connect_error());
    }

    $invalid = 'invalid';

    $stmt = mysqli_prepare($conn, "UPDATE users SET sessionID = ? WHERE sessionID LIKE ?");
    mysqli_stmt_bind_param($stmt, "ss", $invalid, $session_id);

    mysqli_stmt_execute($stmt);

    mysqli_close($conn);

    session_destroy();
?>
