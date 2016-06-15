'use strict';

/**
 * @ngdoc function
 * @name clientApp.controller:ChildrenCtrl
 * @description
 * # ChildrenCtrl
 * Controller of the clientApp
 */
angular.module('clientApp')
  .controller('ChildrenCtrl', function ($scope, $http, $log, alertService, $location, userService) {
	  $scope.editing = false;
	  $scope.adding = false;
	  
	  $scope.genders = [{
		  id: "Select",
		  name: "Selecionar..."
	  	},
	    {
	        id: "Male",
	        name: "Masculino"        
	    }, {
	        id: "Female",
	        name: "Feminino"        
	    }, {
	        id: "Other",
	        name: "Outro"        
	    }];
	  
	  $scope.selected_gendernew = "Select";
	  $scope.selected_gender = [];
	  
	  /* GET - Children list */
	  $scope.getChildren = function() {
       $http.get('app/listchildren')
           .success(function(data) {
        	 angular.forEach(data, function(value, key) {
        		 value.birthDate = new Date(value.birthDate);
        		 $scope.selected_gender.push(value.gender);        		 
        		 var gender = $.grep($scope.genders, function(e){ return e.id === value.gender; });
        		 value.gender = gender[0].name;
        	 });
             $scope.children = data;
           });
     };

     $scope.getChildren();
     
     /* EDIT child */
     $scope.editchild = function(index) {
    	 
    	 var childToEdit = $scope.children[index].childId;
    	 
         var payload = {
           username : $scope.username,          
           firstname : $scope.firstname,
           lastname : $scope.lastname,
           gender : $scope.gender,
           birthdate: $scope.birthdate
         };

         $http.post('app/editchild/' + childToEdit, payload)
             .error(function(data, status) {
               if(status === 400) {
                 angular.forEach(data, function(value, key) {
                   if(key === 'username' || key === 'firstname' || key === 'lastname' || key === 'gender' || key === 'birthdate') {
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
                 $location.path('/children');
               }
             });
       };
       
       /* DELETE child */
       $scope.deletechild = function (index) {
    	   var childToDelete = $scope.children[index].childId;
    	   
    	   $http.post('app/deletechild/'+ childToDelete)
           .error(function(data, status) {
             if(status === 400) {
               angular.forEach(data, function(value, key) {
                 alertService.add('danger', value.message);                 
               });
             }
             if(status === 500) {
               alertService.add('danger', 'Internal server error!');
             }
           })
           .success(function(data) {
             if(data.hasOwnProperty('success')) {                
               $location.path('/children');
             }
           });
       };
       
       /* REGISTER child */
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
                   $location.path('/children');
                 }
               });
         };
  });