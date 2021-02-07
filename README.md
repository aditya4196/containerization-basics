# Commands for docker lab
#### We are using a simple spring boot application for this demo, whose code is commited in this git repo for reference.
For Docker playground use https://labs.play-with-docker.com (avoid katakoda docker playground for build due to memory and speed issues)

### To check the docker version installed
* docker -version

### To list the running containers
* docker ps

### To pull the git project and build the docker image
#### Check the DockerFile used to build the image, in this project root location
* git clone https://github.com/aditya4196/kube-docker-demo.git
* cd kube-docker-demo
* docker build --tag docker-service:2.0 .

### To push built docker image to docker hub - please replace dockerhub_userid with your userid e.g  adi4196
* docker login -u=**dockerhub_userid** # your login to https://hub.docker.com/
* docker tag docker-service:2.0 **dockerhub_userid**/docker-service:2.0
* docker push **dockerhub_userid**/docker-service:2.0 

### To pull the image from the docker hub
* docker pull **docker_userid**/docker-service:2.0 

### To list down the locally stored images
* docker images

### To delete image from local registery
* docker rmi **image_id**

### To create a container from the image pulled
* docker run -dp 8080:8080 **docker_userid**/docker-service:2.0
#### open a new instance in the docker playground (option available in the left section) and hit curl command 'curl get localhost:8080/employees'

### To check running and exited containers
* docker ps -a

### To check the logs of the container
* docker logs **container_id**

### To stop a running container
* docker stop **container_id**

### To kill a running container
* docker kill **container_id**


# Commands for openshift lab
* For Openshift workshop we will use https://www.openshift.com/learn/courses/playground which has pre-configured Openshift Cluster
* Tested image to use here **adi4196/docker-service:2.0** or your own image which you may have just built

### Steps to Login and create Namespace in Openshift:
* oc login -u **developer** -p **developer** 
* oc new-project docker-app #you can skip this and ues the  default myproject as given in katakoda instructions 

#### Pro Tip- use the **maximise button** to get a better experience of the console in openshift playground/katakoda
#### In the git root location, you would also find an Openshift-Lab.pdf which has the steps using the Openshift UI platform
### To pull and run image in Openshift
* oc new-app  aditya4196/docker-service:1.0
### Check the status of the deployment you started
* oc status

### To check the events of the running deployment
* oc get **events**
```
LAST SEEN   TYPE     REASON              OBJECT                                   MESSAGE
2m10s       Normal   Scheduled           pod/docker-service-1-deploy              Successfully assigned docker-app/docker-service-1-deploy to crc-rk2fc-master-0
2m1s        Normal   Pulling             pod/docker-service-1-deploy              Pulling image "quay.io/openshift-release-dev/ocp-v4.0-art-dev@sha256:1d3b5b38c155375ead22a7a7dce941dc7d3a34f58c6d5f69c95d4c8da397bbc0"
117s        Normal   Pulled              pod/docker-service-1-deploy              Successfully pulled image "quay.io/openshift-release-dev/ocp-v4.0-art-dev@sha256:1d3b5b38c155375ead22a7a7dce941dc7d3a34f58c6d5f69c95d4c8da397bbc0"
117s        Normal   Created             pod/docker-service-1-deploy              Created container deployment
117s        Normal   Started             pod/docker-service-1-deploy              Started container deployment
117s        Normal   Scheduled           pod/docker-service-1-sgzr2               Successfully assigned docker-app/docker-service-1-sgzr2 to crc-rk2fc-master-0109s        Normal   Pulling             pod/docker-service-1-sgzr2               Pulling image "ashwinprakash/docker-service@sha256:383dd7f0e828c43539ecc814bb3d968ac015c51b374ec6
```
*UI Steps :- 
*login to console on second tab by popping it out to new window, login with username:- developer, password:- developer , go to workloads-> pods -> docker-service-xxx -> events

### To view the pods running under the namespace after the deployment
* oc get **pods**
```
NAME         READY   STATUS      RESTARTS   AGE
docker-service-1-4v2z6    1/1     Running     0          97s
docker-service-1-deploy   0/1     Completed   0          110s
```
### To check the logs of your application running in the pod
* oc **logs** -f [pod_name]
```
oc logs -f docker-service-1-4v2z6
```
### To view the service created for the deployed application
* oc get **svc**

### To create route for service discovery of the deployed application
* oc expose svc/service_name eg: oc expose **svc/docker-service**

* oc get **route**
```
NAME             HOST/PORT                                                                   PATH   SERVICES         PORT       TERMINATION   WILDCARD
docker-service   docker-service-myproject.2886795281-80-shadow04.environments.katacoda.com          docker-service   8080-tcp                 None
```

### Curl the above hostname over http with uri of /employees to get the data from the actual microservice
* curl **http://docker-service-myproject.2886795281-80-shadow04.environments.katacoda.com/employees**
```

[{"id":1,"firstName":"Pradeep","lastName":"Singh","emailId":"Pradeep.Singh2@gmail.com"},{"id":2,"firstName":"Anand","lastName":"Zaveri","emailId":"Anand.Zaveri@gmail.com"},{"id":3,"firstName":"Akhilesh","lastName":"Juyal","emailId":"Akhilesh.Juyal@gmail.com"},{"id":4,"firstName":"Vaibhav","lastName":"Jha","emailId":"Vaibhav.Jha@outlook.com"},{"id":5,"firstName":"Himanshu","lastName":"Sharma","emailId":"Himanshu.Sharma@yahoo.com"},{"id":6,"firstName":"Jignesh","lastName":"Mehta","emailId":"Jignesh.Mehta@outlook.com"}]

```

### To view all the deployment_configs
* oc get **dc**

### To view the details of the specific deployment_config
* oc describe dc/docker-service 

### To delete the deployment
* oc deploy dc/docker-service --cancel

### To retry the deployment
* oc deploy dc/docker-service --retry


## Openshift Advance Features

### To roll-up or roll-down a pod of the same deployed application
* oc edit dc/docker-service
```
spec:
  replicas: 1 #change the value depending on the no of pods you need
```

UI Steps:-
you can also login to console username;- developer, password:- developer -> workloads -> deployment configs -> press the up arrow to scale upa nd down arrow to scale down- pretty cool eh 

### To create Config Map as environment variable in the container pod
------------------------
* oc create **configmap** boot-env-config **--from-literal=special.employee='Pradeep Singh'**

Note:- Above is imperative model direclty with command

* oc get configmap

#### Edit the deployment config to read env variable from above config map

* oc get dc/docker-service > dc.yaml
Edit the dc.yaml to addd below section just after spec.containers.ports section- make sure to leave 2 spaces else it can break the yaml formatting
```

    spec:
      containers:
        - name: docker-service
          image: >-
            ashwinprakash/docker-service@sha256:625d1259944f6bb46c244bc96ef85bd977f078684f43726bd238d4a9b4a37b07
          ports:
            - containerPort: 8080
              protocol: TCP
          env:
          - name: SPECIAL_EMPLOYEE
            valueFrom:
              configMapKeyRef:
                name: boot-env-config
                key: special.employee
```

 Now we apply the configuration to openshift
 
* oc **apply** -f dc.yaml

#### Lets check the env variable in the pod

* oc get pods | grep **Running**
```
docker-service-2-24glk    1/1     Running     0          17s
```

Lets login to the pod once its up (check with oc get pods - wait for 2 -5 mins for the pdo to be recreated after the apply command) 
* oc rsh docker-service-2-24glk
```
/usr/app # echo $SPECIAL_EMPLOYEE
Pradeep Singh
```
Use the same hostname which we got from oc get route and append the /specialEmp on the http protocl to fetch the same from the underlying container

* curl http://docker-service-myproject.2886795281-80-shadow04.environments.katacoda.com/specialEmp 

### To create secret as environment variable like say to connect to a db from within the pod which we cannot store as a config map sourced from  git
* oc create **secret** generic boot-env-secret **--from-literal=secret.special.employee=Himanshu**

* oc get secret

#### Edit the deployment config to read env variable from above secret
* oc get dc/docker-service > dc.yaml
Edit the dc.yaml to addd below section just after spec.containers.ports section- make sure to leave 2 spaces else it can break the yaml formatting
```

    spec:
      containers:
        - name: docker-service
          image: >-
            ashwinprakash/docker-service@sha256:625d1259944f6bb46c244bc96ef85bd977f078684f43726bd238d4a9b4a37b07
          ports:
            - containerPort: 8080
              protocol: TCP
          env:
          - name: SECRET_EMPLOYEE
            valueFrom:
              secretKeyRef:
                name: boot-env-secret
                key: secret.special.employee
 ```

 Now we apply the configuration to openshift
 
* oc apply -f dc.yaml

Wait for couple of minutes for the new pod to come up - check using oc get pods

Use the same hostname which we got from oc get route and append the /secretEmp on the http protocl to fetch the same from the underlying container

* curl http://docker-service-myproject.2886795281-80-shadow04.environments.katacoda.com/secretEmp
```
Himanshu
```

UI Steps :- 
for secrets- go to workloads-> secrets in the openshift ui
for config maps got to workloads-> config maps

### We can create the config map and secret in the same deployment by making the change in the deployment config as shown below

```
    spec:
      containers:
        - name: docker-service
          image: >-
            ashwinprakash/docker-service@sha256:625d1259944f6bb46c244bc96ef85bd977f078684f43726bd238d4a9b4a37b07
          ports:
            - containerPort: 8080
              protocol: TCP
          env:
		  - name: SPECIAL_EMPLOYEE
            valueFrom:
              configMapKeyRef:
                name: boot-env-config
                key: special.employee
          - name: SECRET_EMPLOYEE
            valueFrom:
              secretKeyRef:
                name: boot-env-secret
                key: secret.employee

```

### To create Blue Green Deployment


* oc new-app adi4196/docker-demo:v1 --name=blueversion
* oc new-app adi4196/docker-demo:v2 --name=greenversion

* oc expose svc/blueversion --name=bluegreen

* oc get route

Hit the browser with hostname/getVersion
Result :- "Old version is Running"

* oc patch route/bluegreen -p '{"spec":{"to":{"name":"greenversion"}}}'

Hit the browser with hostname/getVersion
Result :- "New version is Running"

Note :- hostname will be the same so second time only need to hit the same url
