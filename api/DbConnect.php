<?php

$servername = "localhost";
$username = "ectcoke_test";
$password = "847CzPw?U+SF";
$database = "ectcoke_test";
 
 
$conn = new mysqli($servername, $username, $password, $database);
 
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}