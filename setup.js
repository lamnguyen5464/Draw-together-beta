//set up
const fs = require('fs')
const { exec } = require("child_process");
const path = process.argv[1];
const type = process.argv[2];

const getURL = (callback) => {
	const { url } = require('./backend/config.json');
	if (url) {
		callback?.(url);
		return
	}
	//get local url
	exec("ipconfig getifaddr en0", (error, ip, stderr) => {
		if (error) {
			console.log(`error: ${error.message}`);
			return;
		}
		if (stderr) {
			console.log(`stderr: ${stderr}`);
			return;
		}
		callback?.(`http://${ip.slice(0, -1)}:3000`) 		//erase lineFeed
	});
}

const setupAndroid = () => {
	const androidConfigPath = path.slice(0, path.lastIndexOf("/") + 1) + "android/app/src/main/res/raw/config.properties";
	console.log('androidConfigPath', androidConfigPath)
	getURL((url) => {
		const conetent = `socket_url=${url}`
		fs.writeFile(androidConfigPath, conetent, "utf8", (err) => {
			if (err) {
				console.error(err)
			}
			console.log('config url for android sucessfully')
		})
	})
}

const setupIOS = () => {
	const iosConfigPath = path.slice(0, path.lastIndexOf("/") + 1) + "ios/Canvas-iOS/Utilities/Configs.swift";
	console.log('iosConfigPath', iosConfigPath)
	getURL((url) => {
		const conetent = `public let SOCKET_URI = "${url}";`
		fs.writeFile(iosConfigPath, conetent, "utf8", (err) => {
			if (err) {
				console.error(err)
			}
			console.log('config url for ios sucessfully')
		})
	});
}

const gitCommit = () => {
	const message = process.argv.slice(3).join(" ")
	exec(`git add . && git commit -m \"${message}\"`, (error, ip, stderr) => {
		if (error) {
			console.log(`error: ${error.message}`);
			return;
		}
		if (stderr) {
			console.log(`stderr: ${stderr}`);
			return;
		}
	});

}

console.log('type: ', type)
switch (type) {
	case 'android':
		setupAndroid();
		break;
	case 'ios':
		setupIOS();
		break;
	case 'commit':
		gitCommit()
		break;
}