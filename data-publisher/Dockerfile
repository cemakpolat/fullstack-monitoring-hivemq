FROM python:3.9
ENV PYTHONDONTWRITEBYTECODE 1
ENV PYTHONUNBUFFERED 1
RUN apt-get update && apt-get install -y netcat-openbsd

WORKDIR /app

COPY requirements.txt .
COPY wait-for-mqtt.sh /app/wait-for-mqtt.sh
RUN chmod +x /app/wait-for-mqtt.sh
RUN pip install --no-cache-dir -r requirements.txt

COPY publisher.py .
CMD ["sh", "-c", "/app/wait-for-mqtt.sh && python ./publisher.py"]

