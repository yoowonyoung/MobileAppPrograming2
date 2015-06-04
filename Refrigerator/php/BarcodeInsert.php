<?php
$connect = mysqli_connect ( "mysql.hostinger.kr", "u291047931_admin", "q3tfMk26bU", "u291047931_db" ) or die("db접속에러"); // DB가 있는 주소(이것은 웹서버로 직접 접속하는 것이기 때문에 루프백 주소를 써도 됨)
mysqli_select_db ($connect ,"u291047931_db") or die("DB 선택 에러"); // DB 선택
mysqli_query ( $connect ,"set names utf8" ); // 이것 또한 한글(utf8)을 지원하기 위한 것

$type = $_REQUEST [type];
$code = $_REQUEST [code];
$name = $_REQUEST [name];
$expirydate = $_REQUEST [expirydate];
echo $type;
echo $code;
echo $name;
echo $expirydate;
$qry = "insert into BarcodeInfo (Type, Code, Name, ExpiryDate) values ('$type', $code, '$name', $expirydate)";
$result = mysqli_query ( $connect, $qry );

?>