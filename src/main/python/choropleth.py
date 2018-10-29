import random
import pandas
import folium
from branca.colormap import LinearColormap
import json
import pycountry
import time
import operator
import os
import glob
import sys

def print_top_n_countries(sorted_map_list, N):
    top_N = sorted_map_list[:N]
    print('Top countries:')
    for key, val in top_N:
        print(key + ":" + str(val) + " ", end = '')

def update_map_dict():
    try:
        avail_files = glob.glob('/home/cloudera/Desktop/locations/geolocations_*_*.txt')

        if len(avail_files) == len(processed_files):
            global idle_counter
            idle_counter = idle_counter + 1
            print('Not found any new file for ' + str(idle_counter) + ' minute(s).')
            if idle_counter == 10:
                print('EXITING APPLICATION.')
                sys.exit()
        else:
            for available_file in avail_files:
                if available_file not in processed_files:
                    geolocations = available_file
                    print('\nFound new file: ' + geolocations)
                    processed_files.append(available_file)

                    with open(geolocations, 'r') as geofile:
                        content = geofile.read().splitlines()
                        for code in content:
                            if code == 'null' or code == '':
                                pass
                            else:
                                ccode = pycountry.countries.get(alpha_2=code.upper()).alpha_3
                                if ccode not in map_dict:
                                    map_dict[ccode] = 1
                                else:
                                    map_dict[ccode] = map_dict.get(ccode) + 1
    except:
        pass

    sorted_map_list = sorted(map_dict.items(), key=operator.itemgetter(1), reverse=True)
    print_top_n_countries(sorted_map_list, 10)

def get_color(feature):
    value = map_dict.get(feature['properties']['A3'])
    if value is None:
        return '#ffffff' # MISSING -> white
    else:
        return color_scale(value)

all_codes = pandas.read_csv('https://raw.githubusercontent.com/lukes/ISO-3166-Countries-with-Regional-Codes/master/all/all.csv')
map_dict = {}
sorted_map_list = []
processed_files = []
idle_counter = 0

for code in all_codes['alpha-3']:
    map_dict[code] = 0

while True:
    update_map_dict()
    countries_geo = 'countries-land.json'

    m = folium.Map(
        location = [42.667542, 18.166191],
        tiles = 'Mapbox Bright',
        zoom_start = 2
    )

    m.choropleth(geo_data = countries_geo,
              data = map_dict,
              columns = ['Country', 'Number of Tweets'],
              key_on = 'feature.properties.A3',
              fill_color = 'YlOrRd',
              fill_opacity = 0.7,
              line_opacity = 0.2,
              legend_name = 'Number of Tweets per Country')


    m.save('/media/sf_Git-Repo/twitter-streaming/out/choropleth.html') #/media/sf_Git-Repo/twitter-streaming/out/choropleth.html
    print("\n")
    time.sleep(60)
