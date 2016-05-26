'use strict';

/**
 * @ngdoc function
 * @name clientApp.controller:SignupCtrl
 * @description
 * # SignupCtrl
 * Controller of the clientApp
 */
angular.module('clientApp')
    .controller('SignupCtrl', function ($scope, $http, $log, alertService, $location, userService) {
		$scope.genders = [{
	        id: "Male",
	        name: "Masculino"        
	    }, {
	        id: "Female",
	        name: "Feminino"        
	    }, {
	        id: "Other",
	        name: "Outro"        
	    }];
    	
      $scope.selected_gender = "Other";
		
      $scope.signup = function() {
        var payload = {
          username : $scope.username,
          email : $scope.email,
          password : $scope.password,
          passwordcheck : $scope.passwordcheck,
          firstname : $scope.firstname,
          lastname : $scope.lastname,
          gender : $scope.gender
        };

        $http.post('app/signup', payload)
            .error(function(data, status) {
              if(status === 400) {
                angular.forEach(data, function(value, key) {
                  if(key === 'email' || key === 'password' || key === 'username' || key === 'passwordcheck' || key === 'firstname' || key === 'lastname' || key === 'gender') {
                    alertService.add('danger', key + ' : ' + value);
                  } else {
                    alertService.add('danger', value.message);
                  }
                });
              }
              if(status === 500) {
                alertService.add('danger', 'Internal server error!');
              }
            })
            .success(function(data) {
              if(data.hasOwnProperty('success')) {
                userService.username = $scope.email;
                $location.path('/dashboard');
              }
            });
      };
    });

    