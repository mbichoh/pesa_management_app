<?php

require_once 'DbConnect.php';

	
$response = array();



if(isTheseParametersAvailable(array('plan_name','save_type','deposit','receive', 'user_id', 'date', 'phone'))){
$plan_name  = $_POST['plan_name'];
$save_type  = $_POST['save_type'];
$deposit    = $_POST['deposit'];
$receive    = $_POST['receive'];
$date       = $_POST['date'];
$today      = date("Y-m-d");
$mpesa      = rand(123456789, 987654321);
$user_id    = $_POST['user_id'];
$phone      = $_POST['phone'];

 
    if($receive > $deposit){
        $response['error'] = false; 
        $response['message'] = 'You can`t receive more money than your deposit';
    }else{
        if(strtotime($date) < strtotime($today)){
            $response['error'] = false; 
            $response['message'] = 'Date must be greater than 24hrs from now';
        }else{

         date_default_timezone_set('Africa/Nairobi');

        # access token
        $consumerKey = 'jYpdaqWHzfloGaSXpBElcB0S2D0O54GO';
        $consumerSecret = 'lTq8GENvIqzeMn3L';
        $BusinessShortCode = '174379';
        $Passkey = 'bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919';  
        $PartyA = $phone; // This is your phone number, 
        $AccountReference = 'Pesa Management Sys';
        $TransactionDesc = 'Pesa Management Application System';
        $Amount = $deposit;
        $Timestamp = date('YmdHis');    
        
        # Get the base64 encoded string -> $password. The passkey is the M-PESA Public Key
        $Password = base64_encode($BusinessShortCode.$Passkey.$Timestamp);

        # header for access token
        $headers = ['Content-Type:application/json; charset=utf8'];

            # M-PESA endpoint urls
        $access_token_url = 'https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials';
        $initiate_url = 'https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest';

        # callback url
        $CallBackURL = 'https://ect.co.ke/pma/callback.php';  //set this

        $curl = curl_init($access_token_url);
        curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($curl, CURLOPT_RETURNTRANSFER, TRUE);
        curl_setopt($curl, CURLOPT_HEADER, FALSE);
        curl_setopt($curl, CURLOPT_USERPWD, $consumerKey.':'.$consumerSecret);
        $result = curl_exec($curl);
        $status = curl_getinfo($curl, CURLINFO_HTTP_CODE);
        $result = json_decode($result);
        $access_token = $result->access_token;  
        curl_close($curl);

        # header for stk push
        $stkheader = ['Content-Type:application/json','Authorization:Bearer '.$access_token];

        # initiating the transaction
        $curl = curl_init();
        curl_setopt($curl, CURLOPT_URL, $initiate_url);
        curl_setopt($curl, CURLOPT_HTTPHEADER, $stkheader); //setting custom header

        $curl_post_data = array(
            //Fill in the request parameters with valid values
            'BusinessShortCode' => $BusinessShortCode,
            'Password' => $Password,
            'Timestamp' => $Timestamp,
            'TransactionType' => 'CustomerPayBillOnline',
            'Amount' => $Amount,
            'PartyA' => $PartyA,
            'PartyB' => $BusinessShortCode,
            'PhoneNumber' => $PartyA,
            'CallBackURL' => $CallBackURL,
            'AccountReference' => $AccountReference,
            'TransactionDesc' => $TransactionDesc
        );

        $data_string = json_encode($curl_post_data);
        curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($curl, CURLOPT_POST, true);
        curl_setopt($curl, CURLOPT_POSTFIELDS, $data_string);
        $curl_response = curl_exec($curl);
        $result11 = json_decode($curl_response, true);
        $result11 = $result11['ResponseCode'];

        if($result11 == 0){
            $response['error'] = false; 
            $response['message'] = 'Wait. Processing...'; 

           
            // $stmt = $conn->prepare("INSERT INTO plan(plan_name, save_type, deposit, mpesa_code, receive, date, user_id) VALUES (?, ?, ?, ?, ?, ?, ?)");
            // $stmt->bind_param("ssssssi", $plan_name, $save_type, $deposit,  $mpesa, $receive, $date, $user_id);

            // if($stmt->execute()){
            //     $response['error'] = false; 
            //     $response['message'] = 'Plan registered successfully'; 
            }else{
                $response['error'] = false; 
                $response['message'] = 'Error in transaction process'; 
            }  
        }
    }
}else{
    $response['error'] = true; 
    $response['message'] = 'required parameters are not available'; 
}

function getMpesaResponse($message){
    
    $response['error'] = false; 
    $response['message'] = $message;
}

echo json_encode($response);

function isTheseParametersAvailable($params){
		
    foreach($params as $param){
        if(!isset($_POST[$param])){
            return false; 
        }
    }
    return true; 
}

?>