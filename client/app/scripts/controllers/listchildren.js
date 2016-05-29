'use strict';

/**
 * @ngdoc function
 * @name clientApp.controller:ListchildrenCtrl
 * @description
 * # ListchildrenCtrl
 * Controller of the clientApp
 */
angular.module('clientApp')
  .controller('ListchildrenCtrl', function ($scope, $http, $log, alertService, $location, userService) {
	  $scope.editing = false;
	  
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
	  
	  $scope.selected_gender = [];
	  
	  $scope.getChildren = function() {
       $http.get('app/listchildren')
           .success(function(data) {
        	 angular.forEach(data, function(value, key) {
        		 value.birthDate = new Date(value.birthDate);
        		 $scope.selected_gender.push(value.gender);        		 
        		 var gender = $.grep($scope.genders, function(e){ return e.id == value.gender; });
        		 value.gender = gender[0].name;
        	 });
        	 console.log($scope.selected_gender);
             $scope.children = data;
           });
     };

     $scope.getChildren();
     
     $scope.editchild = function() {
         var payload = {
           username : $scope.username,
           password : $scope.password,
           passwordcheck : $scope.passwordcheck,
           firstname : $scope.firstname,
           lastname : $scope.lastname,
           gender : $scope.gender,
           birthdate: $scope.birthdate
         };

         $http.post('app/editchild', payload)
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
                 $location.path('/listchildren');
               }
             });
       };
  });