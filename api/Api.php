<?php 
 
	require_once 'DbConnect.php';
	
	$response = array();
	
	if(isset($_GET['apicall'])){
		
		switch($_GET['apicall']){
			
			case 'signup':
				if(isTheseParametersAvailable(array('name','email','password','phone'))){
					$name = $_POST['name']; 
					$email = $_POST['email']; 
					$password = md5($_POST['password']);
					$phone = $_POST['phone']; 
					
					$stmt = $conn->prepare("SELECT id FROM users WHERE phone = ? OR email = ?");
					$stmt->bind_param("ss", $phone, $email);
					$stmt->execute();
					$stmt->store_result();
					
					if($stmt->num_rows > 0){
						$stmt->close();
						$response['error'] = true;
						$response['message'] = 'User already registered';
						
					}else{
                        $verifyCode = rand(11111,99999);
                        $vefied = false;
						$stmt = $conn->prepare("INSERT INTO users (name, email, phone, password, code, verified) VALUES (?, ?, ?, ?, ?, ?)");
						$stmt->bind_param("ssssii", $name, $email, $phone,  $password, $verifyCode, $vefied);

						// authentication
						$x_username           = "nathan";
						$x_apikey             = "ami_uWiEED6nNpFZi3JigCUB6d7wzfo4PRaZyEQz1wircdLaL";

						// id of contact to delete
						$params = array(
							"phoneNumbers"=> "+".$phone,
							"message"=>      "Welcome ". $name .". to Pesa Management App. Your verification code is: ".$verifyCode.".",
							"senderId"=>     "", // leave blank if you do not have a custom sender Id
						);

						$data = json_encode($params);

						// endoint
						$sendMessageURL     = "https://api.amisend.com/v1/sms/send";

						$req                  = curl_init($sendMessageURL);

						curl_setopt($req, CURLOPT_CUSTOMREQUEST, "POST");
						curl_setopt($req, CURLOPT_TIMEOUT, 60);
						curl_setopt($req, CURLOPT_SSL_VERIFYPEER, false);
						curl_setopt($req, CURLOPT_RETURNTRANSFER, true);
						curl_setopt($req, CURLOPT_POSTFIELDS, $data);
						curl_setopt($req, CURLOPT_HTTPHEADER, array(
							'Content-Type: application/json',
							'x-api-user: '.$x_username,
							'x-api-key: '.$x_apikey
						));

						// read api response
						$res = curl_exec($req);

						// close curl
						curl_close($req);

						// // print the raw json response
						// var_dump($res);
 
						if($stmt->execute()){
							$stmt = $conn->prepare("SELECT id, id, name, email, phone, code FROM users WHERE phone = ?"); 
							$stmt->bind_param("s",$phone);
							$stmt->execute();
							$stmt->bind_result($userid, $id, $name, $email, $phone, $code);
							$stmt->fetch();
							
							$user = array(
								'id'=>$id, 
								'name'=>$name, 
								'email'=>$email,
								'phone'=>$phone,
								'code'=>$code
							);
							
							$stmt->close();
							
							$response['error'] = false; 
							$response['message'] = 'User registered successfully'; 
							$response['user'] = $user; 
						}
					}
					
				}else{
					$response['error'] = true; 
					$response['message'] = 'required parameters are not available'; 
				}
				
            break; 
            
            case 'verify':
                if(isTheseParametersAvailable(array('code',))){
                    $code = $_POST['code'];
					$codeUpdate = 00000;
					$verifyUpdate = true;
					$notVerified = false;

					$stmt = $conn->prepare("SELECT id FROM users WHERE code = ? AND verified = ?");
					$stmt->bind_param("ii", $code, $notVerified);
					$stmt->execute();
					$stmt->store_result();
					
					if($stmt->num_rows == 0){
						$response['error'] = true;
						$response['message'] = 'Wrong verification code';
						$stmt->close();
					}else{

					$stmt = $conn->prepare("UPDATE  users SET code = ?, verified = ? WHERE code = ?");
					$stmt->bind_param("iii", $codeUpdate, $verifyUpdate, $code);
					$stmt->execute();

					$response['error'] = false; 
					$response['message'] = 'Account Verified';

					$stmt->close();
					}
                }else{
					$response['error'] = true; 
					$response['message'] = 'Invalid input data';
				}
            break;
			
			case 'login':
				
				if(isTheseParametersAvailable(array('phone', 'password'))){
					
					$phone = $_POST['phone'];
                    $password = md5($_POST['password']); 
                    $code_id = 00000;
                    $isVerified = 1;
					
					$stmt = $conn->prepare("SELECT code, verified FROM users WHERE phone = ? AND password = ?");
					$stmt->bind_param("ss",$phone, $password);
					
					$stmt->execute();
					
					$stmt->store_result();
					
					if($stmt->num_rows > 0){

                            $stmt = $conn->prepare("SELECT id, name, email, phone, code FROM users WHERE code = ? AND verified = ? AND phone = ?");
                            $stmt->bind_param("iis",$code_id, $isVerified, $phone);
                            
                            $stmt->execute();
                            
                            $stmt->store_result();

                            if($stmt->num_rows > 0){
						
                                $stmt->bind_result($id, $name, $email, $phone, $code);
                                $stmt->fetch();

                                $user = array(
                                    'id'=>$id, 
                                    'name'=>$name, 
                                    'email'=>$email,
									'phone'=>$phone,
									'code'=>$code
								);
								
								$stmt->close();
                                
                                $response['error'] = false; 
                                $response['message'] = 'Login successful'; 
                                $response['user'] = $user; 
                            }else{
                                $response['error'] = false; 
                                $response['message'] = 'Verify Account';
                            }
					}else{
						$response['error'] = false; 
						$response['message'] = 'Invalid phone or password';
					}
				}
			break; 
			
			default: 
				$response['error'] = true; 
				$response['message'] = 'Invalid Operation Called';
		}
		
	}else{
		$response['error'] = true; 
		$response['message'] = 'Invalid API Call';
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