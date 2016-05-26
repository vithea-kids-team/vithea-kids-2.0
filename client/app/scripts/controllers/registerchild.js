'use strict';

/**
 * @ngdoc function
 * @name clientApp.controller:RegisterChildCtrl
 * @description
 * # RegisterChildCtrl
 * Controller of the clientApp
 */
angular.module('clientApp')
  .controller('RegisterChildCtrl', function ($scope, $http, $log, alertService, $location, userService) {
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
	    
    $scope.registerchild = function() {
        var payload = {
          username : $scope.username,
          password : $scope.password,
          passwordcheck : $scope.passwordcheck,
          firstname : $scope.firstname,
          lastname : $scope.lastname,
          gender : $scope.gender,
          birthdate: $scope.birthdate
        };

        $http.post('app/registerchild', payload)
            .error(function(data, status) {
              if(status === 400) {
                angular.forEach(data, function(value, key) {
                  if(key === 'password' || key === 'username' || key === 'passwordcheck' || key === 'firstname' || key === 'lastname' || key === 'gender' || key === 'birthdate') {
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
                $location.path('/dashboard');
              }
            });
      };
    
  });
