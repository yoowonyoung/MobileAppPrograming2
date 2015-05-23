<?php
$connect = new mysqli ( "mysql.hostinger.kr", "u291047931_admin", "fm8ldlNY4A", "u291047931_db" ) or die ( "db���ӿ���" ); // DB�� �ִ� �ּ�(�̰��� �������� ���� �����ϴ� ���̱� ������ ������ �ּҸ� �ᵵ ��)
mysqli_select_db ( $connect, "u291047931_db" ) or die ( "DB ���� ����" ); // DB ����
mysqli_query ( $connect, "SET NAMES utf8" );

$qry = "SELECT * FROM TestDB";
$result = mysqli_query ( $connect, $qry );
$total_record = $result->num_rows;

echo "{\"status\":\"OK\",\"num_results\":\"$total_record\",\"results\":[";
for($i = 0; $i < $total_record; $i ++) {
	if ($result->data_seek ( $i )) {
		$row = $result->fetch_array ();
		echo "{\"name\":$row[name],\"buyyear \":\"$row[buyyear]\",\"buymonth\":\"$row[buymonth]\",\"buyday \":\"$row[buyday]\",\"limityear\":\"$row[limityear]\",\"limitmonth\":\"$row[limitmonth]\",\"limitday\":\"$row[limitday]\"},";
		if ($i<total_record-1) {
			echo ",";
		}
	} else {
		echo "No Record";
	}
}
echo "]}";
?>
