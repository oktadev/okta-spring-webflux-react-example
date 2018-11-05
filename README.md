# Reactive Spring WebFlux API + Real-time React

This example shows how to build a Reactive Spring WebFlux API, secure it with OIDC, then add real-time capabilities to a React client.

Please read [Build Reactive APIs with Spring WebFlux](https://developer.okta.com/blog/2018/09/24/reactive-apis-with-spring-webflux) to see how this API was created. To see how to build the React app and integrate WebSockets, see [Full Stack Reactive with Spring WebFlux, WebSockets, and React](https://developer.okta.com/blog/2018/09/25/spring-webflux-websockets-react).

**Prerequisites:** [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html), [Maven](https://maven.apache.org), and an [Okta Developer Account](https://developer.okta.com).

> [Okta](https://developer.okta.com/) has Authentication and User Management APIs that reduce development time with instant-on, scalable user infrastructure. Okta's intuitive API and expert support make it easy for developers to authenticate, manage, and secure users and roles in any application.

* [Getting Started](#getting-started)
* [Links](#links)
* [Help](#help)
* [License](#license)

## Getting Started

Clone this application to your local hard drive using Git.

```
git clone https://github.com/oktadeveloper/okta-spring-webflux-react-example.git webflux-api-example
```

To get both the Spring WebFlux API and the React app, you can checkout the `react-app` branch.

```
git clone -b react-app https://github.com/oktadeveloper/okta-spring-webflux-react-example.git full-stack-reactive
```

You will need to create an OIDC Application in Okta to get your settings to log in. 

1. Log in to your developer account on [developer.okta.com](https://developer.okta.com).
2. Navigate to **Applications** and click on **Add Application**.
3. Select **Web** and click **Next**. 
4. Give the application a name (e.g., `Spring WebFlux API`) and add the following as Login redirect URIs:
    * `http://localhost:8080/login/oauth2/code/okta`
    * `https://oidcdebugger.com/debug`
    * `http://localhost:3000/login`
4. Click **Done**, then edit the project and enable "Implicit (Hybrid)" as a grant type (allow ID and access tokens) and click **Save**.

Copy the settings from your OIDC app into `reactive-web/src/main/resources/application.yml`:

```yaml
spring:
  security:
    oauth2:
      client:
        provider:
          okta:
            issuer-uri: https://{yourOktaDomain}/oauth2/default
        registration:
          login:
            okta:
              client-id: {clientId}
              client-secret: {clientSecret}
              scope: openid email profile
```

After making these changes, you should be able to start the app (using `./mvnw` in the `reactive-web` directory), navigate to <http://localhost:8080/profiles> and log in with your Okta credentials.

If you'd like to run the React client (and you've checked out the `react-app` branch), you'll need to modify `react-app/src/App.tsx` to specify your Okta settings.

```typescript
const config = {
  issuer: 'https://{yourOktaDomain}/oauth2/default',
  redirect_uri: window.location.origin + '/implicit/callback',
  client_id: {clientId}
};
```

You can start the React app by running the following commands (in the `react-app` directory):

```bash
npm install
npm start
```

You'll also need to update `reactive-web/src/main/resources/application.yml` to contain a bit more information for enabling an OAuth 2.0 resource server.

```yaml
oidc:
  issuer-uri: https://dev-737523.oktapreview.com/oauth2/default
  client-id: {clientId}
  client-secret: {clientSecret}

spring:
  security:
    oauth2:
      client:
        provider:
          okta:
            issuer-uri: ${oidc.issuer-uri}
        registration:
          login:
            okta:
              client-id: ${oidc.client-id}
              client-secret: ${oidc.client-secret}
              scope: openid email profile
      resourceserver:
        jwt:
          issuer-uri: ${oidc.issuer-uri}
```

## Links

This example uses the following open source libraries:

* [Okta's React SDK](https://github.com/okta/okta-oidc-js/tree/master/packages/okta-react)
* [React](https://reactjs.org/)
* [Spring Boot](https://spring.io/projects/spring-boot)
* [Spring Data](https://spring.io/projects/spring-data)
* [Spring WebFlux](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html)
* [TypeScript](https://www.typescriptlang.org/)

## Help

Please post any questions as comments on the [blog post](https://developer.okta.com/blog/2018/09/24/reactive-apis-with-spring-webflux), or visit our [Okta Developer Forums](https://devforum.okta.com/). You can also email developers@okta.com if you'd like to create a support ticket.

## License

Apache 2.0, see [LICENSE](LICENSE).
