<?php

function My_login($username,$password)
{
    $con_db = new mysqli("localhost", "root", "root", "bus");
    if ($con_db->connect_error) {
        echo "Failed to connect to MySQL: " . $con_db->connect_error;
    }
    $sql_command = "SELECT username,status  FROM users WHERE username='{$username}' and password='{$password}'";
    $result = $con_db->query($sql_command);
    if ($result->num_rows > 0) {
        $row = $result->fetch_assoc();
        $username = $row['username'];
        $status=$row['status'];
        echo 'Succeed;' . $username .';'.$status;

    } else {
        echo 'Failed';
    }
    $con_db->close();
}
function My_regestration($username,$password)
{
    $con_db = new mysqli("localhost", "root", "root", "bus");
    if ($con_db->connect_error) {
        echo "Failed to connect to MySQL: " . $con_db->connect_error;
    }
    $sql_command1 = "SELECT username FROM users WHERE username='{$username}'";
    $result = $con_db->query($sql_command1);
    if ($result->num_rows > 0) {
        echo 'Failed: Username exist.';
    } else {
        $sql_command2 = "INSERT INTO users(username, password) VALUES('{$username}', '{$password}')";
        $con_db->query($sql_command2);
        $sql_command3 = "SELECT username FROM users WHERE username='{$username}' and password='{$password}'";
        $result = $con_db->query($sql_command3);
        if ($result->num_rows > 0) {
            $row = $result->fetch_assoc();
            $username = $row['username'];
            echo 'Succeed;' . $username ;
        }else{
            echo '?';
        }
    }
    $con_db->close();
}

function My_order($onstop,$offstop)
{
    $con_db = new mysqli("localhost", "root", "root", "bus");
    if ($con_db->connect_error) {
        echo "Failed to connect to MySQL: " . $con_db->connect_error;
    }
    $sql_command5 = "SELECT id FROM stops WHERE name='{$offstop}'";
    $result = $con_db->query($sql_command5);
    if ($result->num_rows > 0) {
        $row = $result->fetch_assoc();
        $id = $row['id'];
    }
    $sql_command4="UPDATE stops SET status='y',offstops=concat(offstops,'{$id}',';') WHERE name='{$onstop}' " ;
    $con_db->query($sql_command4);

    echo 'Succeed;' ;
    $con_db->close();
}
function My_location()
{
    $con_db = new mysqli("localhost", "root", "root", "bus");
    if ($con_db->connect_error) {
        echo "Failed to connect to MySQL: " . $con_db->connect_error;
    }
    $sql_command7="SELECT * FROM drivers";
    $result = $con_db->query($sql_command7);
    if ($result->num_rows > 0) {
        while ($row = $result->fetch_assoc()) {
            echo  $row['CurrentStop'] . ';' ;
        }
    }
    else {
        echo 'Failed';
    }
    $con_db->close();
}
function My_news()
{
    $con_db = new mysqli("localhost", "root", "root", "bus");
    if ($con_db->connect_error) {
        echo "Failed to connect to MySQL: " . $con_db->connect_error;
    }
    $sql_command7="SELECT * FROM news order by id DESC limit 1";
    $result = $con_db->query($sql_command7);
    if ($result->num_rows > 0) {
        while ($row = $result->fetch_assoc()) {
            echo  $row['text'] ;
        }
    }
    else {
        echo 'Failed';
    }
    $con_db->close();
}
function My_driving($stop,$username)
{
    $con_db = new mysqli("localhost", "root", "root", "bus");
    if ($con_db->connect_error) {
        echo "Failed to connect to MySQL: " . $con_db->connect_error;
    }
    $sql_commandd0 = "SELECT offstops FROM stops WHERE name='{$stop}'";
    $result = $con_db->query($sql_commandd0);
    if ($result->num_rows > 0) {
        $row = $result->fetch_assoc();
        $offstops = $row['offstops'];
        foreach(explode(';',$offstops) as $number)
        {
            $idnumber=intval($number);
            $sql_commandd2="UPDATE stops SET whomustgo=concat(whomustgo,'{$username}') WHERE id='{$idnumber}'" ;
            $con_db->query($sql_commandd2);
        }
    }


    $sql_commandd1="UPDATE stops SET status='n',offstops='' WHERE name='{$stop}' " ;
    $con_db->query($sql_commandd1);

    $sql_commandd11 = "SELECT whomustgo FROM stops WHERE name='{$stop}'";
    $result111 = $con_db->query($sql_commandd11);

    if ($result111->num_rows > 0) {
        $row111 = $result111->fetch_assoc();
        $whomustgo111 = $row111['whomustgo'];
    }
   
    if($whomustgo111==$username)
    {
        $sql_commandd122="UPDATE stops SET whomustgo='' WHERE name='{$stop}' " ;
        $con_db->query($sql_commandd122);
    }

    else if(strpos($whomustgo111,$username)!==false)
     //if(in_array($username,explode(';',$whomustgo111)))
    {


        $updatewhomustgo=str_replace($username,"",$whomustgo111);

        $sql_commandd122="UPDATE stops SET whomustgo='{$updatewhomustgo}' WHERE name='{$stop}' " ;
        $con_db->query($sql_commandd122);
    }


    $sql_commandd3="UPDATE drivers SET CurrentStop='{$stop}' WHERE username='{$username}' " ;
    $con_db->query($sql_commandd3);

    $sql_commandd4 = "SELECT id FROM stops WHERE name='{$stop}'";
    $result = $con_db->query($sql_commandd4);
    $id=0;
    if ($result->num_rows > 0) {
        $row = $result->fetch_assoc();
        $id = $row['id'];
    }

    $sql_commandd5="SELECT name FROM stops WHERE id = (SELECT MIN(id) FROM stops WHERE id > {$id} AND (status='y' OR whomustgo LIKE '%{$username}%')) ";
    $result1 = $con_db->query($sql_commandd5);
    if ($result1->num_rows > 0) {
        $row1 = $result1->fetch_assoc();
        $stopname = $row1['name'];
        echo 'Succeed;' . $stopname ;
    }
    else {
        $id=$id-26;
        $sql_commandd6="SELECT name FROM stops WHERE id = (SELECT MIN(id) FROM stops WHERE id > {$id} AND (status='y' OR whomustgo LIKE '%{$username}%')) ";

        $result1 = $con_db->query($sql_commandd6);
        if ($result1->num_rows > 0) {
            $row1 = $result1->fetch_assoc();
            $stopname = $row1['name'];
            echo 'Succeed;' . $stopname ;
        }
        else{
            echo 'Failed';
        }
    }
//    $sql_command7="SELECT * FROM news order by id DESC limit 1";
//    $result = $con_db->query($sql_command7);
//    if ($result->num_rows > 0) {
//        while ($row = $result->fetch_assoc()) {
//            echo  $row['text'] ;
//        }
//    }
//    else {
//        echo 'Failed';
//    }
    $con_db->close();
}

function My_publish($news)
{
    $con_db = new mysqli("localhost", "root", "root", "bus");
    if ($con_db->connect_error) {
        echo "Failed to connect to MySQL: " . $con_db->connect_error;
    }

        $sql_command2 = "INSERT INTO news(text) VALUES('{$news}')";
        $con_db->query($sql_command2);

        echo 'Succeed;';


    $con_db->close();
}





$method=$_POST['method'];
switch($method){
    case 'loginp':
        $username=$_POST['username'];
        $password=$_POST['password'];
        My_login($username,$password);
        break;
    case 'register':
        $username=$_POST['username'];
        $password=$_POST['password'];
        My_regestration($username,$password);
        break;
    case'order':
        $onstop=$_POST['onstop'];
        $offstop=$_POST['offstop'];
        My_order($onstop,$offstop);
        break;
    case'location':
        My_location();
        break;
    case'news':
        My_news();
        break;
    case'driving':
        $stop=$_POST['stop'];
        $username=$_POST['username'];
        My_driving($stop,$username);
        break;
    case'publish':
        $news=$_POST['news'];
        My_publish($news);
        break;
//    case 'update':
//        $username=$_POST['username'];
//        $fullname=$_POST['fullname'];
//        $interest=$_POST['interest'];
//        $freq=$_POST['freq'];
//        $ori_username=$_POST['ori_username'];
//        My_update($username,$fullname,$interest,$freq,$ori_username);
//        break;
//    case 'mac':
//        $username=$_POST['username'];
//        $mac=$_POST['mac'];
//        $wifi=$_POST['wifi'];
//        My_mac($username,$mac,$wifi);
//        break;
//    case 'search':
//        My_search();
//        break;
//    case 'location':
//        $mac_location=$_POST['mac_location'];
//        My_location($mac_location);
//        break;
//    case 'getnews':
//        $interest=$_POST['interest'];
//        My_getnews($interest);
    default:
        break;
}
?>