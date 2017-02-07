from flask import *
import time, string, os, json

app = Flask(__name__)

SCORES = []
if os.path.exists('scores.json'):
	with open('scores.json','rb') as f:
		SCORES = json.load(f)

permittedCharacters = string.letters+string.digits + '-_'

lastPruning = time.time()

@app.route('/')
def indexPage():
	return "Hellooo!"

@app.route('/recentscores.ld')
def getRecentScores():
	most_recent = SCORES[-5:][::-1]
	output = ''
	for i in most_recent:
		output += i['name'] + ' ' + str(i['score']) + '\n'
	return Response(output, mimetype='text/plain')

@app.route('/allscores.ld')
def allScores():
	def generate():
		yield 'I have ' + str(len(SCORES)) + ' records.\n'
		for score in SCORES:
			yield score['name'] + ' ' + str(score['score']) + ' ' + str(score['when']) + '\n'

	return Response(generate(), mimetype='text/plain')

@app.route('/highest.ld')
def highestScores():
	highest = list(SCORES)
	highest.sort(key=lambda sc:sc['score'])
	highest = highest[::-1][-10:]

	def generate():
		for score in highest:
			yield score['name'] + ' ' + str(score['score']) + ' ' + str(score['when']) + '\n'

	return Response(generate(), mimetype='text/plain')



@app.route('/submitscore.ld')
def submitScore():
	if not 'Java/' in request.headers.get('User-Agent'):
		return Response('false\n', mimetype='text/plain')
	name = request.args.get('name')
	if not name or len(name) > 16 or len(name) < 3:
		return Response('false\nname must be between 3 and 16 characters long', mimetype='text/plain')

	for c in name:
		if not c in permittedCharacters:
			return Response('false\nonly letters, digits, - and _ allowed', mimetype='text/plain')

	if not request.args.get('score'):
		return Response('false', mimetype='text/plain')

	score = int(request.args.get('score'))

	if score <= 500:
		return Response('false\nscore not high enough', mimetype='text/plain')



	SCORES.append({
		'name': name,
		'score': score,
		'addr': request.remote_addr,
		'when': int(time.time())
	})

	with open('scores.json','wb') as f:
		json.dump(SCORES, f)

	return Response('true', mimetype='text/plain')

@app.route('/save/')
def saveScoresToDisk():
	time.sleep(5)
	with open('scores.json','wb') as f:
		json.dump(SCORES, f)
	return 'Ok.'

if __name__ == '__main__':
	app.run(debug=True, host='0.0.0.0', port=43934, threaded=True)
