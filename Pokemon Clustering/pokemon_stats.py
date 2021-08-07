#Author: Anshul
#Credits: CS540 Discord
import csv
import sys
import numpy as np


def load_data(filepath):
    dataset = list()
    with open(filepath, 'r', encoding='UTF8') as file:
        dictReader = csv.DictReader(file)
        count = 0
        for pokemon in dictReader:
            if count >= 20:
                break
            del pokemon["Generation"], pokemon["Legendary"]
            attributeToIntMapping = ["#", "Total", "HP", "Attack", "Defense", "Sp. Atk", "Sp. Def", "Speed"]
            for attribute in pokemon:
                if attribute in attributeToIntMapping:
                    pokemon[attribute] = int(pokemon[attribute])
            dataset.append(pokemon)
            count += 1
    return dataset


def calculate_x_y(stats):
    x = stats.get("Attack") + stats.get("Sp. Atk") + stats.get("Speed")
    y = stats.get("HP") + stats.get("Defense") + stats.get("Sp. Def")
    return x, y


def euclidean_distance(dp1, dp2):
    return np.sqrt((dp1[0] - dp2[0]) ** 2 + (dp1[1] - dp2[1]) ** 2)


def hac(dataset):
    z = list()
    for i in range(19):
        z.append(list())
        for j in range(4):
            z[i].append(None)

    clusters = list()
    merged = set()

    for i in range(20):
        cluster = list()
        cluster.append(dataset[i])
        clusters.append(cluster)

    for iteration in range(19):
        smallestClusterDistance = sys.maxsize
        for i in range(len(clusters)):
            if i not in merged:
                for j in range(i + 1, len(clusters)):
                    if j not in merged:
                        clusterDistance = sys.maxsize
                        for poke1 in clusters[i]:
                            for poke2 in clusters[j]:
                                distance = euclidean_distance(poke1, poke2)
                                if distance < clusterDistance:
                                    clusterDistance = distance
                        if clusterDistance < smallestClusterDistance:
                            smallestClusterDistance = clusterDistance
                            closestPokes = (i, j)

        cluster = list()
        cluster.extend(clusters[closestPokes[0]])
        cluster.extend(clusters[closestPokes[1]])
        clusters.append(cluster)
        z[iteration][0] = closestPokes[0]
        z[iteration][1] = closestPokes[1]
        z[iteration][2] = smallestClusterDistance
        z[iteration][3] = len(cluster)
        merged.add(closestPokes[0])
        merged.add(closestPokes[1])

    return np.array(z)


