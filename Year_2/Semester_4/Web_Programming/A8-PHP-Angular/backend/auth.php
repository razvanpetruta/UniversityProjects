<?php
    header("Access-Control-Allow-Origin: http://localhost:4200");
    header("Access-Control-Allow-Headers: Content-Type");
    header("Access-Control-Allow-Methods: POST");

    $json = file_get_contents('php://input');
    $_POST = json_decode($json, true);

    if (!isset($_POST["session_id"])) {
        $error_data = array('error' => 'Session ID not provided');
        echo json_encode($error_data);
        exit();
    }

    $session_id = $_POST['session_id'];

    session_id($session_id);
    session_start();

    $conn = mysqli_connect("localhost", "root", "", "testdb");

    $query = "SELECT username, role FROM users WHERE sessionID = ?";
    $stmt = mysqli_prepare($conn, $query);
    mysqli_stmt_bind_param($stmt, "s", $session_id);
    mysqli_stmt_execute($stmt);
    mysqli_stmt_bind_result($stmt, $user, $role);
    mysqli_stmt_fetch($stmt);
    mysqli_stmt_close($stmt);

    mysqli_close($conn);

    if (!isset($user)) {
        exit();
    }
?>
