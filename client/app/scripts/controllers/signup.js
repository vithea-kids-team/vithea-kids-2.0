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
			id: "Select",
			name: "Selecionar..."
	  	}, {
				id: "Male",
				name: "Masculino"
			}, {
				id: "Female",
				name: "Feminino"
			}, {
				id: "Other",
				name: "Outro"
			}];

		/* Select gender dropdown list handler for creation */
		$scope.selectnewgender = function (item) {

			$scope.selected_gender = item;
			$scope.gender = $scope.selected_gender.id;
    }

		$scope.selected_gender = $scope.genders[0];
		$scope.gender = $scope.selected_gender.id;
    	
		
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

    