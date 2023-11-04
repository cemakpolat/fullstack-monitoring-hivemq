FROM mcr.microsoft.com/dotnet/sdk:5.0 AS build
WORKDIR /src

COPY csharp-subscriber.csproj .
RUN dotnet restore

COPY . .
RUN dotnet publish -c Release -o /app

FROM mcr.microsoft.com/dotnet/runtime:5.0
RUN apt-get update && apt-get install -y netcat-openbsd
WORKDIR /app
COPY --from=build /app .
COPY wait-for-mqtt.sh /app/wait-for-mqtt.sh
RUN chmod +x /app/wait-for-mqtt.sh
CMD ["sh", "-c", "/app/wait-for-mqtt.sh && dotnet csharp-subscriber.dll"]

