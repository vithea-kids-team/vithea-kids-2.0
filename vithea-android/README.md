# Vithea Kids 2.0

Virtual Therapist For Austim Treatment on Children is an application for helping children with ASD to improve their language and communication
skills. 

## Technically Speaking 

VITHEA Kids has three modules:
1. vithea-kids-api is a REST API created using Play Framework
2. vithea-kids-caregiver is a Web Application created using Angular 2
3. vithea-kids-child is an Android application

## Running
In the 'server' folder to start serving the vithea-kids-api:
`activator run`

In the 'client' folder to start serving vithea-kids-caregiver:
`npm start`

## Deploying
In the 'server' folder to deploy vithea-kids-api:
`activator dist`

In the 'client' folder to deploy vithea-kids-caregiver:
`ng build --e=prod --t=production`

## Contributing
1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D
