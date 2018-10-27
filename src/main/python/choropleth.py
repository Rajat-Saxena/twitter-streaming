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

def update_map_dict(old_timestamp):
    try:
        geolocations = '/home/cloudera/Desktop/locations/geolocations.txt'
        avail_files = glob.glob('/home/cloudera/Desktop/locations/geolocations.txt_*_*')
        current_timestamp = os.stat(geolocations).st_mtime_ns
        #old_timestamp=1

        print('Orig:' + str(old_timestamp))
        print('Current:' + str(current_timestamp))

        if current_timestamp != old_timestamp:
            print('Found new file. Updating map.')
            old_timestamp = current_timestamp
            with open(geolocations, 'r') as geofile:
                content = geofile.read().splitlines()
                for code in content:
                    if code == 'null' or code == '':
                        pass
                    else:
                        ccode = pycountry.countries.get(alpha_2=code.upper()).alpha_3
                        if ccode not in map_dict:
                            #print("ccode " + ccode + " not present in dictionary. Adding.")
                            map_dict[ccode] = 1
                        else:
                            #print("ccode " + ccode + " present in dictionary. Updating.")
                            map_dict[ccode] = map_dict.get(ccode) + 1
        else:
            print('Same file. Not updating map.')
    except:
        pass

    sorted_map_list = sorted(map_dict.items(), key=operator.itemgetter(1), reverse=True)
    print(sorted_map_list[:10])
    return old_timestamp

def get_color(feature):
    value = map_dict.get(feature['properties']['A3'])
    if value is None:
        return '#ffffff' # MISSING -> white
    else:
        return color_scale(value)

all_codes = pandas.read_csv('https://raw.githubusercontent.com/lukes/ISO-3166-Countries-with-Regional-Codes/master/all/all.csv')
map_dict = {}
old_timestamp = 100
sorted_map_list = []
processed_files = []

for code in all_codes['alpha-3']:
    map_dict[code] = 0

while True:
    old_timestamp = update_map_dict(old_timestamp)
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
    print("Map updated.")
    time.sleep(60)
