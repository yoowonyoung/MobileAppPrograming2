<?php
$connect = mysqli_connect ( "mysql.hostinger.kr", "u291047931_admin", "q3tfMk26bU", "u291047931_db" ) or die("db���ӿ���"); // DB�� �ִ� �ּ�(�̰��� �������� ���� �����ϴ� ���̱� ������ ������ �ּҸ� �ᵵ ��)
mysqli_select_db ($connect ,"u291047931_db") or die("DB ���� ����"); // DB ����
mysqli_query ( $connect ,"set names utf8" ); // �̰� ���� �ѱ�(utf8)�� �����ϱ� ���� ��

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