<?php  
    header("Access-Control-Allow-Origin: http://localhost:4200");
    header("Access-Control-Allow-Headers: Content-Type");
    header("Access-Control-Allow-Methods: POST");
    
    $formUser = $_POST['user'];
    $formPassword = $_POST['password'];
    $role;

    $link = mysqli_connect("localhost", "root", "", "testdb");

    $stmt = mysqli_prepare($link, "SELECT username, password, role FROM users WHERE username = ? and password = ?");
    mysqli_stmt_bind_param($stmt, "ss", $formUser, $formPassword);
    
    mysqli_stmt_execute($stmt);
    mysqli_stmt_bind_result($stmt, $dbUser, $dbPassword, $role);

    if (!mysqli_stmt_fetch($stmt)) {
        http_response_code(401); 
        $error_data = array('error' => 'Invalid credentials'); 
        echo json_encode($error_data); 
        exit();
    }	

    mysqli_stmt_close($stmt);
    mysqli_close($link);

    session_start(); 

    $_SESSION['user'] = $formUser;
    $_SESSION['role'] = $role;
    $sessionID = session_id();

    $data = array(
        'user' => $formUser,
        'role' => $role,
        'session_id' => $sessionID
    );
    
    $json_data = json_encode($data);    

    $conn = mysqli_connect("localhost", "root", "", "testdb");

    if (!$conn) {
        die("Connection failed: " . mysqli_connect_error());
    }

    $stmt = mysqli_prepare($conn, "UPDATE users SET sessionID = ? WHERE username = ?");
    mysqli_stmt_bind_param($stmt, "ss", $sessionID, $formUser);

    mysqli_stmt_execute($stmt);

    mysqli_close($conn);
    
    echo $json_data;
?>
