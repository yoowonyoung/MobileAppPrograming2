<?php
$connect = mysqli_connect ( "mysql.hostinger.kr", "u291047931_admin", "fm8ldlNY4A", "u291047931_db" ) or die("db���ӿ���"); // DB�� �ִ� �ּ�(�̰��� �������� ���� �����ϴ� ���̱� ������ ������ �ּҸ� �ᵵ ��)
mysqli_select_db ($connect ,"u291047931_db") or die("DB ���� ����"); // DB ����
mysqli_query ( $connect ,"set names utf8" ); // �̰� ���� �ѱ�(utf8)�� �����ϱ� ���� ��

$type = $_REQUEST [type];
$code = $_REQUEST [code];
$name = $_REQUEST [name];
$expirydate = $_REQUEST [expirydate];

$qry = "insert into BarcodeInfo (Type, Code, Name, ExpiryDate) values ('$type', $code, '$name', $expirydate)";
$result = mysqli_query ( $connect, $qry );

?>