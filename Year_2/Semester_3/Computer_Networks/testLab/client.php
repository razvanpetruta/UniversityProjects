<?php

$s = socket_create(AF_INET, SOCK_STREAM, 0);
socket_connect($s, "server ip", 8888);

while (TRUE) {
    $n = (string) readline("Enter the size: ");
    socket_send($s, $n, 1, 0);

    for ($i = 1; $i <= $n; $i++) {
        $character = (string) readline("Character: ");
        socket_send($s, $character, 1, 0);
    }
    socket_recv($s, $buf, 100, 0);
    echo $buf."\n";
}

?>